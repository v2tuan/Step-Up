package com.stepup.service.impl;

import com.stepup.Enum.OrderShippingStatus;
import com.stepup.components.SecurityUtils;
import com.stepup.dtos.requests.OrderDTO;
import com.stepup.dtos.requests.OrderItemDTO;
import com.stepup.entity.*;
import com.stepup.repository.*;
import com.stepup.service.IOderItemService;
import com.stepup.service.IOderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OderServiceImpl implements IOderService {
    @Autowired
    private OderRepository oderRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private IOderItemService oderItemService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private CouponRepository couponRepo;
    @Autowired
    private ProductVariantRepository productVariantRepo;

    @Override
    public List<Order> getAllOrders() {
        return oderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long orderId) {
        return oderRepository.findById(orderId);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return oderRepository.findByUser_Id(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderShippingStatus status) {
//        return repo.findByStatus(status);
        return null;
    }

    @Override
    public Order saveOrder(Order order) {
        return oderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderShippingStatus status) {
        Optional<Order> orderOpt = oderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
//            order.setStatus(status);
            return oderRepository.save(order);
        }
        return null;
    }

    @Override
    public void deleteOrderById(Long orderId) {
        oderRepository.deleteById(orderId);
    }

    @Override
    public Double getTotalRevenue() {
//        return repo.getTotalRevenue();
        return 0.0;
    }

    @Override
    public Double getRevenueByStatus(OrderShippingStatus status) {
//        return repo.getRevenueByStatus(status);
        return null;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) {
        User loginUser = securityUtils.getLoggedInUser();
        Order order = new Order();
        order.setUser(loginUser);
        order.setAddress(addressRepo.findById(orderDTO.getAddressId())
                .orElseThrow(() -> new RuntimeException("Chưa có địa chỉ nhận hàng")));
        order.setPaymentMethod(orderDTO.getPaymentMethod());

        Coupon coupon = null;
        if (orderDTO.getCouponId() != null) {
            coupon = couponRepo.findById(orderDTO.getCouponId()).orElse(null);
            order.setCoupon(coupon);
        }

        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setStatus(OrderShippingStatus.PENDING);
            ProductVariant productVariant = productVariantRepo.findById(orderItemDTO.getProductVariantId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm tương ứng trong đơn hàng"));

            orderItem.setProductVariant(productVariant);
            orderItem.setPrice(productVariant.getPrice());
            orderItem.setPromotionPrice(productVariant.getPromotionPrice());
            orderItem.setCount(orderItemDTO.getCount());
            orderItem.setShippingPrice(30000.0);
            double itemTotal = productVariant.getPromotionPrice() * orderItemDTO.getCount();
            orderItem.setSubTotal(itemTotal);
            orderItems.add(orderItem);
            totalAmount += itemTotal;
        }

        double discount = 0.0;

        if (coupon != null) {
            for (CouponCondition condition : coupon.getCouponConditionList()) {
                switch (condition.getAttribute()) {
                    case MINIMUM_AMOUNT:
                        if (totalAmount < Double.parseDouble(condition.getValue())) {
                            throw new RuntimeException("Giá trị đơn hàng không đạt tối thiểu");
                        }
                        break;
                    case EXPIRY:
                        LocalDate expiryDate = LocalDate.parse(condition.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (LocalDate.now().isAfter(expiryDate)) {
                            throw new RuntimeException("Coupon đã hết hạn");
                        }
                        break;
                    case DISCOUNT:
                        discount = (Double.parseDouble(condition.getValue()) / 100) * totalAmount;
                        break;
                }
            }
        }

        if (discount > 0 && totalAmount > 0) {
            double remainingDiscount = discount;
            int lastIndex = orderItems.size() - 1;

            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem orderItem = orderItems.get(i);
                if (i == lastIndex) {
                    orderItem.setDiscountPrice(remainingDiscount);
                } else {
                    double proportion = orderItem.getSubTotal() / totalAmount;
                    double discountPrice = Math.round(discount * proportion * 100.0) / 100.0;
                    orderItem.setTotalPrice(orderItem.getSubTotal() - discountPrice);
                    orderItem.setDiscountPrice(discountPrice);
                    remainingDiscount -= discountPrice;
                }
            }
        }

        order.setDiscountPrice(discount);
        order.setSubTotal(totalAmount);
        order.setTotalPrice(totalAmount - discount);
        order.setOrderItems(orderItems);
        return oderRepository.save(order);
    }

}

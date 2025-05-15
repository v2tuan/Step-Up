// Khởi tạo trình xử lý gửi form khi DOM được tải
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.querySelector('.login-form');

    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    // Xử lý các nút đăng nhập bằng mạng xã hội
    const socialButtons = document.querySelectorAll('.login-social-button');
    socialButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Có thể mở rộng để xử lý riêng cho từng nhà cung cấp đăng nhập xã hội
            alert('Chức năng đăng nhập bằng mạng xã hội sẽ được triển khai trong tương lai');
        });
    });
});

/**
 * Xử lý việc gửi form đăng nhập
 * @param {Event} event - Sự kiện submit
 */
async function handleLogin(event) {
    event.preventDefault();

    // Lấy giá trị từ form
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const rememberMe = document.getElementById('remember');

    // Kiểm tra cơ bản
    if (!emailInput.value.trim()) {
        showError('Vui lòng nhập email của bạn');
        emailInput.focus();
        return;
    }

    if (!passwordInput.value) {
        showError('Vui lòng nhập mật khẩu của bạn');
        passwordInput.focus();
        return;
    }

    try {
        // Hiển thị trạng thái đang tải
        const submitButton = event.target.querySelector('.login-form-submit');
        const originalButtonText = submitButton.textContent;
        submitButton.disabled = true;
        submitButton.textContent = 'Đang đăng nhập...';

        // Chuẩn bị dữ liệu đăng nhập - phải khớp với UserLoginDTO
        const loginData = {
            email: emailInput.value.trim(),
            password: passwordInput.value
        };

        console.log('Gửi request login tới endpoint:', '/api/v1/users/login');
        console.log('Dữ liệu đăng nhập:', JSON.stringify(loginData));

        // Đường dẫn API - đúng với endpoint trong Spring Boot
        const loginEndpoint = '/api/v1/users/login'; // Sử dụng chính xác endpoint từ Spring @PostMapping("/api/v1/users/login")

        // Gọi API đăng nhập
        const response = await fetch(loginEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        console.log('Trạng thái phản hồi:', response.status, response.statusText);

        if (!response.ok) {
            // Xử lý phản hồi lỗi
            let errorMessage;
            try {
                const errorData = await response.json();
                errorMessage = errorData.message || `Đăng nhập thất bại: ${response.status} ${response.statusText}`;
            } catch (e) {
                // Nếu response không phải JSON
                errorMessage = `Đăng nhập thất bại: ${response.status} ${response.statusText}`;
            }
            throw new Error(errorMessage);
        }

        // Phân tích phản hồi thành công với token JWT
        const data = await response.json();
        console.log('Đăng nhập thành công, nhận được dữ liệu:', Object.keys(data));

        if (data && data.token) {
            // Lưu trữ thông tin từ token
            const tokenData = parseJwt(data.token);
            console.log('Thông tin từ token:', tokenData);

            // Lưu JWT token vào cookie
            const expirationDays = rememberMe && rememberMe.checked ? 30 : 1;
            setAuthCookie('auth_token', data.token, expirationDays);

            // Lưu thông tin người dùng từ JWT nếu có
            if (tokenData && tokenData.email) {
                localStorage.setItem('user_email', tokenData.email);
            }

            // Chuyển hướng đến trang chat admin
            window.location.href = '/admin/chat';
        } else {
            throw new Error('Phản hồi không hợp lệ từ máy chủ, không có token');
        }
    } catch (error) {
        console.error('Lỗi đăng nhập:', error);
        showError(error.message || 'Đã xảy ra lỗi trong quá trình đăng nhập. Vui lòng thử lại.');

        // Đặt lại trạng thái nút
        const submitButton = event.target.querySelector('.login-form-submit');
        submitButton.disabled = false;
        submitButton.textContent = 'Đăng nhập';
    }
}

/**
 * Phân tích JWT để trích xuất thông tin
 * @param {string} token - JWT token
 * @returns {Object} Dữ liệu được giải mã từ token
 */
function parseJwt(token) {
    try {
        // JWT có cấu trúc: header.payload.signature
        // Chúng ta cần payload (phần thứ hai)
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('Lỗi khi phân tích JWT:', e);
        return null;
    }
}

/**
 * Đặt cookie xác thực
 * @param {string} name - Tên cookie
 * @param {string} value - Giá trị cookie (token JWT)
 * @param {number} days - Số ngày cho đến khi hết hạn
 */
function setAuthCookie(name, value, days) {
    const expires = new Date();
    expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
    document.cookie = `${name}=${value};expires=${expires.toUTCString()};path=/;SameSite=Strict;`;
}

/**
 * Hiển thị thông báo lỗi
 * @param {string} message - Thông báo lỗi để hiển thị
 */
function showError(message) {
    // Kiểm tra xem phần tử lỗi đã tồn tại chưa, nếu chưa thì tạo mới
    let errorElement = document.querySelector('.login-error');

    if (!errorElement) {
        errorElement = document.createElement('div');
        errorElement.className = 'login-error';
        errorElement.style.color = '#e53e3e';
        errorElement.style.fontSize = '14px';
        errorElement.style.marginBottom = '16px';
        errorElement.style.padding = '8px 12px';
        errorElement.style.backgroundColor = 'rgba(229, 62, 62, 0.1)';
        errorElement.style.borderRadius = '6px';
        errorElement.style.textAlign = 'center';

        // Chèn sau phần header của form
        const loginHeader = document.querySelector('.login-header');
        if (loginHeader && loginHeader.nextElementSibling) {
            loginHeader.parentNode.insertBefore(errorElement, loginHeader.nextElementSibling);
        }
    }

    errorElement.textContent = message;
    errorElement.style.display = 'block';

    // Tự động ẩn sau 5 giây
    setTimeout(() => {
        errorElement.style.display = 'none';
    }, 5000);
}

/**
 * Lấy giá trị của cookie theo tên
 * @param {string} name - Tên cookie
 * @returns {string|null} Giá trị cookie hoặc null nếu không tìm thấy
 */
function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

// /**
//  * Kiểm tra xem người dùng đã được xác thực chưa và chuyển hướng nếu cần
//  */
// function checkAuthentication() {
//     const token = getCookie('auth_token');
//     if (token) {
//         // Người dùng đã đăng nhập, chuyển hướng đến trang chat
//         window.location.href = '/admin/chat';
//     }
// }

// /**
//  * Kiểm tra xác thực và xử lý chuyển hướng phù hợp
//  * @param {string} currentPath - Đường dẫn hiện tại
//  */
// function checkAuthentication() {
//     const currentPath = window.location.pathname;
//     const token = getCookie('auth_token');
//     const isLoginPage = currentPath === '/login' || currentPath === '/login.html';
//
//     if (token) {
//         // Người dùng đã đăng nhập
//         if (isLoginPage) {
//             // Nếu đang ở trang login mà đã có token, chuyển hướng đến trang chính
//             window.location.href = '/admin/chat';
//         }
//         // Nếu không phải trang login và có token, giữ nguyên (đã đăng nhập đúng)
//     } else {
//         // Người dùng chưa đăng nhập
//         if (!isLoginPage) {
//             // Nếu đang không ở trang login và không có token, chuyển hướng đến trang login
//             // Lưu URL hiện tại để sau khi đăng nhập có thể quay lại
//             const returnUrl = encodeURIComponent(window.location.href);
//             window.location.href = `/login?returnUrl=${returnUrl}`;
//         }
//         // Nếu đang ở trang login và không có token, giữ nguyên (đúng trạng thái)
//     }
// }

/**
 //  * Kiểm tra xác thực và xử lý chuyển hướng phù hợp
 //  * @param {string} currentPath - Đường dẫn hiện tại
 //  */
function checkAuthentication() {
}

/**
 * Đăng xuất người dùng
 * Xóa token JWT và thông tin người dùng
 */
function logout() {
    // Xóa cookie xác thực
    document.cookie = 'auth_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    // Xóa dữ liệu người dùng từ localStorage
    localStorage.removeItem('user_email');
    // Chuyển hướng về trang đăng nhập
    window.location.href = '/login';
}

// Kiểm tra xác thực khi trang tải
checkAuthentication();
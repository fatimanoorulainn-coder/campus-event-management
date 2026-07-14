/**
 * Campus Event Management System - Main JavaScript
 * Handles UI interactions, animations, and form validations
 */

// ============================================
// DOM Ready
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    console.log('Campus Event Management System loaded');
    
    // Initialize all components
    initTooltips();
    initToastNotifications();
    initFormValidation();
    initPasswordToggle();
    initLoadingStates();
    initAutoDismissAlerts();
    initEventCardAnimations();
});

// ============================================
// Tooltips
// ============================================
function initTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// ============================================
// Toast Notifications
// ============================================
function initToastNotifications() {
    // Show toast for success/error messages
    const toastEls = document.querySelectorAll('.toast');
    toastEls.forEach(function(toastEl) {
        const toast = new bootstrap.Toast(toastEl, {
            autohide: true,
            delay: 5000
        });
        toast.show();
    });
}

/**
 * Show a toast notification
 * @param {string} message - The message to display
 * @param {string} type - 'success', 'error', 'warning', 'info'
 */
function showToast(message, type = 'info') {
    const colors = {
        success: 'bg-success',
        error: 'bg-danger',
        warning: 'bg-warning',
        info: 'bg-info'
    };
    
    const icons = {
        success: 'fa-check-circle',
        error: 'fa-exclamation-circle',
        warning: 'fa-exclamation-triangle',
        info: 'fa-info-circle'
    };
    
    const toastContainer = document.querySelector('.toast-container') || createToastContainer();
    
    const toastHtml = `
        <div class="toast align-items-center text-white border-0 ${colors[type] || 'bg-secondary'}" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas ${icons[type] || 'fa-info-circle'} me-2"></i>
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    `;
    
    toastContainer.insertAdjacentHTML('beforeend', toastHtml);
    
    const toastEl = toastContainer.lastElementChild;
    const toast = new bootstrap.Toast(toastEl, {
        autohide: true,
        delay: 5000
    });
    toast.show();
    
    // Remove toast after hidden
    toastEl.addEventListener('hidden.bs.toast', function() {
        this.remove();
    });
}

function createToastContainer() {
    const container = document.createElement('div');
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
    document.body.appendChild(container);
    return container;
}

// ============================================
// Form Validation
// ============================================
function initFormValidation() {
    // Bootstrap form validation
    const forms = document.querySelectorAll('.needs-validation');
    
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} True if valid
 */
function isValidEmail(email) {
    const re = /^[A-Za-z0-9+_.-]+@(.+)$/;
    return re.test(email);
}

/**
 * Validate phone number format
 * @param {string} phone - Phone to validate
 * @returns {boolean} True if valid
 */
function isValidPhone(phone) {
    const re = /^[0-9+\-\s()]{10,15}$/;
    return re.test(phone);
}

/**
 * Validate password strength
 * @param {string} password - Password to validate
 * @returns {object} { valid: boolean, message: string }
 */
function validatePassword(password) {
    if (!password || password.length < 6) {
        return { valid: false, message: 'Password must be at least 6 characters' };
    }
    if (!/[A-Z]/.test(password)) {
        return { valid: false, message: 'Password must contain at least one uppercase letter' };
    }
    if (!/[a-z]/.test(password)) {
        return { valid: false, message: 'Password must contain at least one lowercase letter' };
    }
    if (!/[0-9]/.test(password)) {
        return { valid: false, message: 'Password must contain at least one number' };
    }
    return { valid: true, message: 'Password is strong' };
}

// ============================================
// Password Toggle
// ============================================
function initPasswordToggle() {
    const toggleButtons = document.querySelectorAll('[id^="togglePassword"]');
    
    toggleButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            const input = this.closest('.input-group').querySelector('input');
            const icon = this.querySelector('i');
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.className = 'fas fa-eye-slash';
            } else {
                input.type = 'password';
                icon.className = 'fas fa-eye';
            }
        });
    });
}

// ============================================
// Loading States
// ============================================
function initLoadingStates() {
    // Show loading spinner on form submit
    const forms = document.querySelectorAll('form');
    
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            const spinner = form.querySelector('.spinner-border');
            
            if (submitBtn && spinner) {
                submitBtn.disabled = true;
                spinner.classList.remove('d-none');
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span> Processing...';
            }
        });
    });
}

/**
 * Show loading overlay
 * @param {string} elementId - ID of element to overlay
 */
function showLoading(elementId) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const overlay = document.createElement('div');
    overlay.className = 'loading-overlay';
    overlay.innerHTML = `
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    `;
    overlay.style.cssText = `
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 16px;
        z-index: 10;
    `;
    element.style.position = 'relative';
    element.appendChild(overlay);
}

/**
 * Hide loading overlay
 * @param {string} elementId - ID of element
 */
function hideLoading(elementId) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const overlay = element.querySelector('.loading-overlay');
    if (overlay) {
        overlay.remove();
    }
}

// ============================================
// Auto-dismiss Alerts
// ============================================
function initAutoDismissAlerts() {
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(function(alert) {
        // Auto dismiss after 5 seconds for success alerts
        if (alert.classList.contains('alert-success')) {
            setTimeout(function() {
                const closeBtn = alert.querySelector('.btn-close');
                if (closeBtn) {
                    closeBtn.click();
                }
            }, 5000);
        }
    });
}

// ============================================
// Event Card Animations
// ============================================
function initEventCardAnimations() {
    const cards = document.querySelectorAll('.event-card');
    
    cards.forEach(function(card, index) {
        card.style.animationDelay = (index * 0.1) + 's';
    });
}

// ============================================
// Confirmation Dialog
// ============================================
/**
 * Show a confirmation dialog
 * @param {string} message - Confirmation message
 * @param {string} title - Dialog title
 * @returns {Promise<boolean>} Resolves with user's choice
 */
function confirmAction(message, title = 'Confirm Action') {
    return new Promise((resolve) => {
        const modalHtml = `
            <div class="modal fade" id="confirmModal" tabindex="-1">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content glass-card">
                        <div class="modal-header">
                            <h5 class="modal-title text-white">
                                <i class="fas fa-exclamation-triangle text-warning me-2"></i>${title}
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <p class="text-white">${message}</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-danger" id="confirmBtn">Confirm</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', modalHtml);
        const modalEl = document.getElementById('confirmModal');
        const modal = new bootstrap.Modal(modalEl);
        
        modalEl.addEventListener('hidden.bs.modal', function() {
            this.remove();
            resolve(false);
        });
        
        modalEl.querySelector('#confirmBtn').addEventListener('click', function() {
            modal.hide();
            resolve(true);
        });
        
        modal.show();
    });
}

// ============================================
// Form Data Helper
// ============================================
/**
 * Get form data as object
 * @param {HTMLFormElement} form - Form element
 * @returns {object} Form data object
 */
function getFormData(form) {
    const formData = new FormData(form);
    const data = {};
    for (let [key, value] of formData.entries()) {
        data[key] = value;
    }
    return data;
}

/**
 * Set form data from object
 * @param {HTMLFormElement} form - Form element
 * @param {object} data - Data object
 */
function setFormData(form, data) {
    for (let key in data) {
        const input = form.querySelector(`[name="${key}"]`);
        if (input) {
            input.value = data[key];
        }
    }
}

// ============================================
// URL Helpers
// ============================================
/**
 * Get URL parameters as object
 * @returns {object} URL parameters
 */
function getUrlParams() {
    const params = new URLSearchParams(window.location.search);
    const data = {};
    for (let [key, value] of params.entries()) {
        data[key] = value;
    }
    return data;
}

/**
 * Update URL parameters without reload
 * @param {object} params - Parameters to update
 */
function updateUrlParams(params) {
    const url = new URL(window.location);
    for (let key in params) {
        if (params[key]) {
            url.searchParams.set(key, params[key]);
        } else {
            url.searchParams.delete(key);
        }
    }
    window.history.pushState({}, '', url);
}

// ============================================
// Export for use in other scripts
// ============================================
window.showToast = showToast;
window.confirmAction = confirmAction;
window.showLoading = showLoading;
window.hideLoading = hideLoading;
window.isValidEmail = isValidEmail;
window.isValidPhone = isValidPhone;
window.validatePassword = validatePassword;
window.getFormData = getFormData;
window.setFormData = setFormData;
window.getUrlParams = getUrlParams;
window.updateUrlParams = updateUrlParams;

console.log('✅ Campus Event Management System - JavaScript initialized');
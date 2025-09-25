/**
 * Sistema de Aluguel de Carros - JavaScript Utils
 * Funcionalidades interativas para melhorar a experiência do usuário
 */

// ===== UTILITÁRIOS GLOBAIS =====
window.SistemaCarro = {
    // Formatar CPF
    formatCPF: function(cpf) {
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    },

    // Formatar CNPJ
    formatCNPJ: function(cnpj) {
        return cnpj.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    },

    // Formatar moeda
    formatCurrency: function(value) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    },

    // Formatar data
    formatDate: function(date) {
        return new Date(date).toLocaleDateString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    // Mostrar notificação toast
    showToast: function(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `toast-notification toast-${type}`;
        toast.innerHTML = `
            <div class="toast-content">
                <span class="toast-icon">${this.getToastIcon(type)}</span>
                <span class="toast-message">${message}</span>
                <button class="toast-close" onclick="this.parentElement.parentElement.remove()">&times;</button>
            </div>
        `;
        
        document.body.appendChild(toast);
        
        // Animar entrada
        setTimeout(() => toast.classList.add('show'), 100);
        
        // Auto remover após 5 segundos
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 5000);
    },

    // Ícones para toast
    getToastIcon: function(type) {
        const icons = {
            'success': '✅',
            'error': '❌',
            'warning': '⚠️',
            'info': 'ℹ️'
        };
        return icons[type] || icons.info;
    },

    // Confirmar ação com modal customizado
    confirm: function(message, callback) {
        const modal = document.createElement('div');
        modal.className = 'custom-confirm-modal';
        modal.innerHTML = `
            <div class="custom-confirm-backdrop">
                <div class="custom-confirm-content">
                    <div class="custom-confirm-header">
                        <h5>⚠️ Confirmação</h5>
                    </div>
                    <div class="custom-confirm-body">
                        <p>${message}</p>
                    </div>
                    <div class="custom-confirm-footer">
                        <button class="btn btn-secondary" onclick="SistemaCarro.closeConfirm(false)">Cancelar</button>
                        <button class="btn btn-danger" onclick="SistemaCarro.closeConfirm(true)">Confirmar</button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.appendChild(modal);
        this.confirmCallback = callback;
        
        // Animar entrada
        setTimeout(() => modal.classList.add('show'), 100);
    },

    // Fechar modal de confirmação
    closeConfirm: function(confirmed) {
        const modal = document.querySelector('.custom-confirm-modal');
        if (modal) {
            modal.classList.remove('show');
            setTimeout(() => modal.remove(), 300);
        }
        
        if (this.confirmCallback) {
            this.confirmCallback(confirmed);
            this.confirmCallback = null;
        }
    }
};

// ===== FUNCIONALIDADES DE TABELA =====
class TableManager {
    constructor(tableId, options = {}) {
        this.table = document.getElementById(tableId);
        this.tbody = this.table?.querySelector('tbody');
        this.searchInput = options.searchInput;
        this.filterSelect = options.filterSelect;
        this.sortable = options.sortable || false;
        
        if (this.table) {
            this.init();
        }
    }

    init() {
        this.setupSearch();
        this.setupFilter();
        this.setupSort();
        this.setupPagination();
    }

    setupSearch() {
        if (this.searchInput) {
            const input = document.getElementById(this.searchInput);
            if (input) {
                input.addEventListener('input', (e) => {
                    this.search(e.target.value);
                });
            }
        }
    }

    setupFilter() {
        if (this.filterSelect) {
            const select = document.getElementById(this.filterSelect);
            if (select) {
                select.addEventListener('change', (e) => {
                    this.filter(e.target.value);
                });
            }
        }
    }

    setupSort() {
        if (this.sortable) {
            const headers = this.table.querySelectorAll('thead th[data-sortable]');
            headers.forEach(header => {
                header.style.cursor = 'pointer';
                header.addEventListener('click', () => {
                    this.sort(header.dataset.sortable, header.dataset.sortDirection === 'asc' ? 'desc' : 'asc');
                    
                    // Update visual indicators
                    headers.forEach(h => h.classList.remove('sort-asc', 'sort-desc'));
                    header.classList.add(header.dataset.sortDirection === 'asc' ? 'sort-desc' : 'sort-asc');
                    header.dataset.sortDirection = header.dataset.sortDirection === 'asc' ? 'desc' : 'asc';
                });
            });
        }
    }

    search(term) {
        const rows = this.tbody.querySelectorAll('tr');
        let visibleCount = 0;
        
        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            const matches = text.includes(term.toLowerCase());
            
            row.style.display = matches ? '' : 'none';
            if (matches) visibleCount++;
        });
        
        this.updateCounter(visibleCount);
    }

    filter(value) {
        const rows = this.tbody.querySelectorAll('tr');
        let visibleCount = 0;
        
        rows.forEach(row => {
            const matches = !value || row.dataset.filter === value || row.textContent.toLowerCase().includes(value.toLowerCase());
            
            row.style.display = matches ? '' : 'none';
            if (matches) visibleCount++;
        });
        
        this.updateCounter(visibleCount);
    }

    sort(column, direction) {
        const rows = Array.from(this.tbody.querySelectorAll('tr'));
        const sortedRows = rows.sort((a, b) => {
            const aValue = a.querySelector(`[data-sort-${column}]`)?.textContent || 
                          a.cells[parseInt(column)]?.textContent || '';
            const bValue = b.querySelector(`[data-sort-${column}]`)?.textContent || 
                          b.cells[parseInt(column)]?.textContent || '';
            
            if (direction === 'asc') {
                return aValue.localeCompare(bValue, 'pt-BR', { numeric: true });
            } else {
                return bValue.localeCompare(aValue, 'pt-BR', { numeric: true });
            }
        });
        
        // Re-append sorted rows
        sortedRows.forEach(row => this.tbody.appendChild(row));
    }

    updateCounter(count) {
        const counter = document.getElementById('totalCount');
        if (counter) {
            counter.textContent = count;
        }
    }

    setupPagination() {
        // Implementação futura para paginação
    }
}

// ===== VALIDAÇÃO DE FORMULÁRIOS =====
class FormValidator {
    constructor(formId) {
        this.form = document.getElementById(formId);
        this.rules = {};
        
        if (this.form) {
            this.init();
        }
    }

    init() {
        this.form.addEventListener('submit', (e) => {
            if (!this.validate()) {
                e.preventDefault();
            }
        });
    }

    addRule(fieldId, rule) {
        this.rules[fieldId] = rule;
    }

    validate() {
        let isValid = true;
        
        Object.keys(this.rules).forEach(fieldId => {
            const field = document.getElementById(fieldId);
            const rule = this.rules[fieldId];
            
            if (field && !this.validateField(field, rule)) {
                isValid = false;
            }
        });
        
        return isValid;
    }

    validateField(field, rule) {
        const value = field.value.trim();
        let isValid = true;
        let message = '';
        
        // Required
        if (rule.required && !value) {
            isValid = false;
            message = 'Este campo é obrigatório';
        }
        
        // CPF
        if (rule.cpf && value && !this.isValidCPF(value)) {
            isValid = false;
            message = 'CPF inválido';
        }
        
        // CNPJ
        if (rule.cnpj && value && !this.isValidCNPJ(value)) {
            isValid = false;
            message = 'CNPJ inválido';
        }
        
        // Email
        if (rule.email && value && !this.isValidEmail(value)) {
            isValid = false;
            message = 'Email inválido';
        }
        
        this.showValidationMessage(field, isValid, message);
        return isValid;
    }

    isValidCPF(cpf) {
        cpf = cpf.replace(/[^\d]/g, '');
        if (cpf.length !== 11) return false;
        
        // Verificar se todos os dígitos são iguais
        if (/^(\d)\1+$/.test(cpf)) return false;
        
        // Validar dígitos verificadores
        let sum = 0;
        for (let i = 0; i < 9; i++) {
            sum += parseInt(cpf.charAt(i)) * (10 - i);
        }
        
        let digit = 11 - (sum % 11);
        if (digit === 10 || digit === 11) digit = 0;
        
        if (digit !== parseInt(cpf.charAt(9))) return false;
        
        sum = 0;
        for (let i = 0; i < 10; i++) {
            sum += parseInt(cpf.charAt(i)) * (11 - i);
        }
        
        digit = 11 - (sum % 11);
        if (digit === 10 || digit === 11) digit = 0;
        
        return digit === parseInt(cpf.charAt(10));
    }

    isValidCNPJ(cnpj) {
        cnpj = cnpj.replace(/[^\d]/g, '');
        if (cnpj.length !== 14) return false;
        
        // Verificar se todos os dígitos são iguais
        if (/^(\d)\1+$/.test(cnpj)) return false;
        
        // Validar dígitos verificadores
        const weights1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
        const weights2 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
        
        let sum = 0;
        for (let i = 0; i < 12; i++) {
            sum += parseInt(cnpj.charAt(i)) * weights1[i];
        }
        
        let digit = 11 - (sum % 11);
        if (digit === 10 || digit === 11) digit = 0;
        
        if (digit !== parseInt(cnpj.charAt(12))) return false;
        
        sum = 0;
        for (let i = 0; i < 13; i++) {
            sum += parseInt(cnpj.charAt(i)) * weights2[i];
        }
        
        digit = 11 - (sum % 11);
        if (digit === 10 || digit === 11) digit = 0;
        
        return digit === parseInt(cnpj.charAt(13));
    }

    isValidEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }

    showValidationMessage(field, isValid, message) {
        // Remove mensagens anteriores
        const existingMessage = field.parentNode.querySelector('.validation-message');
        if (existingMessage) {
            existingMessage.remove();
        }
        
        // Atualizar classe do campo
        field.classList.remove('is-valid', 'is-invalid');
        field.classList.add(isValid ? 'is-valid' : 'is-invalid');
        
        // Mostrar mensagem de erro
        if (!isValid && message) {
            const messageDiv = document.createElement('div');
            messageDiv.className = 'validation-message text-danger small mt-1';
            messageDiv.textContent = message;
            field.parentNode.appendChild(messageDiv);
        }
    }
}

// ===== LOADING E FEEDBACK =====
class LoadingManager {
    static show(message = 'Carregando...') {
        this.hide(); // Remove loader existente
        
        const loader = document.createElement('div');
        loader.id = 'loading-overlay';
        loader.innerHTML = `
            <div class="loading-content">
                <div class="loading-spinner"></div>
                <div class="loading-message">${message}</div>
            </div>
        `;
        
        document.body.appendChild(loader);
        setTimeout(() => loader.classList.add('show'), 10);
    }

    static hide() {
        const loader = document.getElementById('loading-overlay');
        if (loader) {
            loader.classList.remove('show');
            setTimeout(() => loader.remove(), 300);
        }
    }

    static showButton(button, message = 'Processando...') {
        button.dataset.originalText = button.textContent;
        button.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span>${message}`;
        button.disabled = true;
    }

    static hideButton(button) {
        if (button.dataset.originalText) {
            button.textContent = button.dataset.originalText;
            button.disabled = false;
            delete button.dataset.originalText;
        }
    }
}

// ===== INICIALIZAÇÃO =====
document.addEventListener('DOMContentLoaded', function() {
    // Atualizar relógio em tempo real
    function updateClock() {
        const clockElements = document.querySelectorAll('#currentTime, .current-time');
        clockElements.forEach(element => {
            if (element) {
                element.textContent = new Date().toLocaleString('pt-BR');
            }
        });
    }
    
    updateClock();
    setInterval(updateClock, 1000);

    // Inicializar tooltips do Bootstrap
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }

    // Smooth scrolling para links âncora
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });

    // Auto-hide alerts após 5 segundos
    document.querySelectorAll('.alert').forEach(alert => {
        setTimeout(() => {
            if (alert.parentNode) {
                alert.style.transition = 'opacity 0.5s';
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 500);
            }
        }, 5000);
    });

    // Confirmar exclusões com modal customizado
    document.querySelectorAll('form[action*="/excluir"]').forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            SistemaCarro.confirm('Tem certeza que deseja excluir este item?', (confirmed) => {
                if (confirmed) {
                    LoadingManager.show('Excluindo...');
                    this.submit();
                }
            });
        });
    });
});

// ===== EXPORTAR PARA GLOBAL =====
window.TableManager = TableManager;
window.FormValidator = FormValidator;
window.LoadingManager = LoadingManager;
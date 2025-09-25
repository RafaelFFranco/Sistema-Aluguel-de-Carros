/**
 * Sistema de Aluguel de Carros - Interactive JavaScript
 * Funcionalidades interativas para melhorar a experiência do usuário
 */

document.addEventListener('DOMContentLoaded', function() {
    
    // Inicializar tooltips do Bootstrap
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Inicializar popovers do Bootstrap
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-dismiss alerts após 5 segundos
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Smooth scroll para âncoras
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // Animação de números (contadores)
    const counters = document.querySelectorAll('.counter');
    const animateCounters = () => {
        counters.forEach(counter => {
            const target = parseInt(counter.getAttribute('data-target')) || parseInt(counter.innerText);
            const count = parseInt(counter.innerText);
            const increment = target / 100;

            if (count < target) {
                counter.innerText = Math.ceil(count + increment);
                setTimeout(animateCounters, 20);
            } else {
                counter.innerText = target;
            }
        });
    };

    // Observador de interseção para animações
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animate-in');
                
                // Iniciar animação de contadores
                if (entry.target.querySelector('.counter')) {
                    animateCounters();
                }
            }
        });
    }, observerOptions);

    // Observar elementos com animação
    document.querySelectorAll('.fade-in-up, .stats-card, .welcome-card').forEach(el => {
        observer.observe(el);
    });

    // Validação de formulários personalizados
    const forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Loading states para botões
    const loadingButtons = document.querySelectorAll('.btn-loading');
    loadingButtons.forEach(button => {
        button.addEventListener('click', function() {
            this.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status"></span>Carregando...';
            this.disabled = true;
        });
    });

    // Filtros de tabela melhorados
    const searchInputs = document.querySelectorAll('[data-table-filter]');
    searchInputs.forEach(input => {
        const targetTable = document.querySelector(input.getAttribute('data-table-filter'));
        if (targetTable) {
            input.addEventListener('keyup', function() {
                const filter = this.value.toLowerCase();
                const rows = targetTable.querySelectorAll('tbody tr');
                
                rows.forEach(row => {
                    const text = row.textContent.toLowerCase();
                    row.style.display = text.includes(filter) ? '' : 'none';
                });

                // Atualizar contadores se existirem
                updateTableCounters(targetTable);
            });
        }
    });

    // Função para atualizar contadores de tabela
    function updateTableCounters(table) {
        const visibleRows = table.querySelectorAll('tbody tr:not([style*="display: none"])');
        const totalRows = table.querySelectorAll('tbody tr');
        
        const counter = document.querySelector(`[data-table-counter="${table.id}"]`);
        if (counter) {
            counter.textContent = `${visibleRows.length} de ${totalRows.length}`;
        }
    }

    // Confirmação de exclusão melhorada
    const deleteButtons = document.querySelectorAll('[data-confirm-delete]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm-delete') || 'Tem certeza que deseja excluir?';
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
        });
    });

    // Formatação de campos de entrada
    const cpfInputs = document.querySelectorAll('input[data-mask="cpf"]');
    cpfInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            e.target.value = value;
        });
    });

    const phoneInputs = document.querySelectorAll('input[data-mask="phone"]');
    phoneInputs.forEach(input => {
        input.addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            value = value.replace(/(\d{2})(\d)/, '($1) $2');
            value = value.replace(/(\d{4})(\d)/, '$1-$2');
            value = value.replace(/(\d{4})-(\d)(\d{4})/, '$1$2-$3');
            e.target.value = value;
        });
    });

    // Status indicators com atualização automática
    const statusIndicators = document.querySelectorAll('[data-status-check]');
    if (statusIndicators.length > 0) {
        setInterval(() => {
            statusIndicators.forEach(indicator => {
                const url = indicator.getAttribute('data-status-check');
                fetch(url)
                    .then(response => response.json())
                    .then(data => {
                        indicator.className = `badge ${data.success ? 'bg-success' : 'bg-danger'}`;
                        indicator.textContent = data.message;
                    })
                    .catch(() => {
                        indicator.className = 'badge bg-warning';
                        indicator.textContent = 'Verificando...';
                    });
            });
        }, 30000); // Verificar a cada 30 segundos
    }
});

// Utilitários globais
window.SacUtils = {
    // Formatar moeda brasileira
    formatCurrency: function(value) {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);
    },

    // Formatar data brasileira
    formatDate: function(date) {
        return new Intl.DateTimeFormat('pt-BR').format(new Date(date));
    },

    // Copiar texto para clipboard
    copyToClipboard: function(text) {
        navigator.clipboard.writeText(text).then(function() {
            // Mostrar toast de sucesso
            const toast = document.createElement('div');
            toast.className = 'toast-notification';
            toast.textContent = 'Copiado para a área de transferência!';
            document.body.appendChild(toast);
            
            setTimeout(() => {
                document.body.removeChild(toast);
            }, 3000);
        });
    },

    // Mostrar loading overlay
    showLoading: function() {
        const loading = document.createElement('div');
        loading.id = 'loading-overlay';
        loading.innerHTML = `
            <div class="loading-spinner">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Carregando...</span>
                </div>
            </div>
        `;
        document.body.appendChild(loading);
    },

    // Esconder loading overlay
    hideLoading: function() {
        const loading = document.getElementById('loading-overlay');
        if (loading) {
            document.body.removeChild(loading);
        }
    }
};
// scripts/cadastro-sala.js

// === VALIDAÇÃO DO FORMULÁRIO ===
function inicializarValidacaoFormulario() {
    'use strict';

    // Seleciona todos os formulários que precisam de validação
    var forms = document.querySelectorAll('.needs-validation');

    // Loop sobre eles e previne submissão
    Array.prototype.slice.call(forms)
        .forEach(function(form) {
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
}

// === INICIALIZAÇÃO DA PÁGINA CADASTRO SALA ===
function inicializarCadastroSala() {
    inicializarValidacaoFormulario();
}

// Exportar funções
window.cadastroSala = {
    inicializar: inicializarCadastroSala
};
// scripts/main.js

// === INICIALIZAÇÃO GERAL DA APLICAÇÃO ===
document.addEventListener('DOMContentLoaded', function() {
    console.log('Sistema de Agendamento de Salas - Inicializando...');

    // Detectar qual página está sendo carregada e inicializar os scripts correspondentes
    const path = window.location.pathname;

    if (path.includes('/agendamento/novo') || path.includes('/agendamento/editar')) {
        // Página de agendamento
        if (typeof agendar !== 'undefined') {
            agendar.configurarEventos();
            agendar.inicializar();
        }
    } else if (path.includes('/agendamento')) {
        // Página de listagem de agendamentos
        if (typeof agendamentos !== 'undefined') {
            agendamentos.inicializar();
        }
    } else if (path.includes('/sala/cadastro') || path.includes('/sala/editar')) {
        // Página de cadastro/edição de sala
        if (typeof cadastroSala !== 'undefined') {
            cadastroSala.inicializar();
        }
    } else if (path.includes('/sala')) {
        // Página de listagem de salas
        if (typeof salas !== 'undefined') {
            salas.inicializar();
        }
    }

    // Inicializar componentes comuns a todas as páginas
    inicializarComponentesComuns();
});

// === COMPONENTES COMUNS A TODAS AS PÁGINAS ===
function inicializarComponentesComuns() {
    // Inicializar tooltips do Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Inicializar popovers do Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    const popoverList = popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

// === FUNÇÕES UTILITÁRIAS GLOBAIS ===
window.utils = {
    // Função para mostrar loading
    mostrarLoading: function() {
        // Implementar overlay de loading se necessário
    },

    // Função para esconder loading
    esconderLoading: function() {
        // Implementar remoção do overlay de loading
    },

    // Função para formatar data
    formatarData: function(dataISO) {
        const data = new Date(dataISO);
        return data.toLocaleDateString('pt-BR');
    }
};
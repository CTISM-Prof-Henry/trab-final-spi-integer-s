document.addEventListener('DOMContentLoaded', function() {
    console.log('Sistema de Agendamento de Salas - Inicializando...');

    const path = window.location.pathname;

    if (path.includes('/agendamento/novo') || path.includes('/agendamento/editar')) {
        if (typeof agendar !== 'undefined') {
            agendar.configurarEventos();
            agendar.inicializar();
        }
    } else if (path.includes('/agendamento')) {
        if (typeof agendamentos !== 'undefined') {
            agendamentos.inicializar();
        }
    } else if (path.includes('/sala/cadastro') || path.includes('/sala/editar')) {
        if (typeof cadastroSala !== 'undefined') {
            cadastroSala.inicializar();
        }
    } else if (path.includes('/sala')) {
        if (typeof salas !== 'undefined') {
            salas.inicializar();
        }
    }

    inicializarComponentesComuns();
});

function inicializarComponentesComuns() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    const tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    const popoverList = popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

window.utils = {
    mostrarLoading: function() {
        // Implementar overlay de loading se necessário
    },

    esconderLoading: function() {
        // Implementar remoção do overlay de loading
    },

    formatarData: function(dataISO) {
        const data = new Date(dataISO);
        return data.toLocaleDateString('pt-BR');
    }
};
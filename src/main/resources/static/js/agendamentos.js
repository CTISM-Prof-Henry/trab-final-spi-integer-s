// scripts/agendamentos.js

// === FUNÇÕES GLOBAIS ===
function formatarData(dataISO) {
    const data = new Date(dataISO);
    return data.toLocaleDateString('pt-BR');
}

// === INICIALIZAÇÃO DA PÁGINA AGENDAMENTOS ===
function inicializarAgendamentos() {
    const hoje = new Date().toISOString().split('T')[0];
    const dataAtualElement = document.getElementById('data-atual');

    if (dataAtualElement) {
        dataAtualElement.textContent = formatarData(hoje);
    }

    // Preencher filtros com valores atuais (se existirem)
    const urlParams = new URLSearchParams(window.location.search);
    const dataFiltro = urlParams.get('data');
    const statusFiltro = urlParams.get('status');
    const blocoFiltro = urlParams.get('bloco');

    if (dataFiltro && dataAtualElement) {
        document.getElementById('data-filter').value = dataFiltro;
        dataAtualElement.textContent = formatarData(dataFiltro);
    }
    if (statusFiltro) {
        document.getElementById('status-filter').value = statusFiltro;
    }
    if (blocoFiltro) {
        document.getElementById('bloco-filter').value = blocoFiltro;
    }

    // Auto-refresh a cada 2 minutos (opcional)
    setTimeout(() => {
        window.location.reload();
    }, 120000);
}

// Exportar funções
window.agendamentos = {
    inicializar: inicializarAgendamentos,
    formatarData: formatarData
};

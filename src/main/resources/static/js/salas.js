// scripts/salas.js

// === FILTROS DA TABELA ===
function filtrarTabela() {
    const filtroCapacidade = parseInt(document.getElementById('capacidade-filter').value) || 0;
    const filtroTipo = document.getElementById('tipo-filter').value.toLowerCase();
    const filtroStatus = document.getElementById('status-filter').value.toLowerCase();
    const linhas = document.querySelectorAll('tbody tr');

    linhas.forEach(linha => {
        if (linha.cells.length > 1) {
            const capacidadeTexto = linha.cells[3].textContent;
            const capacidade = parseInt(capacidadeTexto) || 0;
            const tipo = linha.cells[2].textContent.toLowerCase();
            const status = linha.cells[4].textContent.toLowerCase();

            const mostraCapacidade = capacidade >= filtroCapacidade;
            const mostraTipo = !filtroTipo || tipo.includes(filtroTipo);
            const mostraStatus = !filtroStatus ||
                (filtroStatus === 'livre' && status.includes('livre')) ||
                (filtroStatus === 'em-uso' && status.includes('em uso'));

            linha.style.display = (mostraCapacidade && mostraTipo && mostraStatus) ? '' : 'none';
        }
    });
}

function limparFiltros() {
    document.getElementById('capacidade-filter').value = '0';
    document.getElementById('tipo-filter').value = '';
    document.getElementById('status-filter').value = '';
    filtrarTabela();
}

// === ATUALIZAR HORA ATUAL ===
function atualizarHora() {
    const agora = new Date();
    const hora = agora.getHours().toString().padStart(2, '0');
    const minutos = agora.getMinutes().toString().padStart(2, '0');
    const horaAtualElement = document.getElementById('hora-atual');

    if (horaAtualElement) {
        horaAtualElement.textContent = hora + ':' + minutos;
    }
}

// === CONFIRMAÇÃO DE EXCLUSÃO ===
function configurarBotoesExclusao() {
    const botoesExcluir = document.querySelectorAll('.btn-excluir');

    botoesExcluir.forEach(botao => {
        botao.addEventListener('click', function(e) {
            const salaId = this.getAttribute('data-sala-id');
            const salaBloco = this.getAttribute('data-sala-bloco');
            const confirmacao = confirm('Tem certeza que deseja excluir a sala ' + salaBloco + ' ' + salaId + '?');

            if (!confirmacao) {
                e.preventDefault();
            }
        });
    });
}

// === INICIALIZAÇÃO DA PÁGINA SALAS ===
function inicializarSalas() {
    configurarBotoesExclusao();

    // Atualizar hora inicial a cada minuto
    atualizarHora();
    setInterval(atualizarHora, 60000);

    // Aplicar filtros quando os selects mudarem
    const capacidadeFilter = document.getElementById('capacidade-filter');
    const tipoFilter = document.getElementById('tipo-filter');
    const statusFilter = document.getElementById('status-filter');

    if (capacidadeFilter) {
        capacidadeFilter.addEventListener('change', filtrarTabela);
    }
    if (tipoFilter) {
        tipoFilter.addEventListener('change', filtrarTabela);
    }
    if (statusFilter) {
        statusFilter.addEventListener('change', filtrarTabela);
    }

    // Auto-refresh para atualizar os status
    setTimeout(() => {
        window.location.reload();
    }, 120000);
}

// Exportar funções
window.salas = {
    inicializar: inicializarSalas,
    filtrarTabela: filtrarTabela,
    limparFiltros: limparFiltros,
    atualizarHora: atualizarHora
};

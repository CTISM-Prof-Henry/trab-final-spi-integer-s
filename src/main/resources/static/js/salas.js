// === FILTROS DA TABELA ===
function filtrarTabela() {
    const filtroCapacidade = parseInt(document.getElementById('capacidade-filter').value) || 0;
    const filtroTipo = document.getElementById('tipo-filter').value.toLowerCase().trim();
    const filtroStatus = document.getElementById('status-filter').value.toLowerCase().trim();

    const linhas = document.querySelectorAll('tbody tr');

    linhas.forEach(linha => {
        const celulas = linha.querySelectorAll('td');
        if (celulas.length < 5) return; // ignora linhas sem dados válidos

        const capacidadeTexto = celulas[3].textContent.trim();
        const capacidade = parseInt(capacidadeTexto) || 0;
        const tipo = celulas[2].textContent.toLowerCase().trim();
        const status = celulas[4].innerText.toLowerCase().replace(/\s+/g, ' ').trim();

        const mostraCapacidade = capacidade >= filtroCapacidade;
        const mostraTipo = !filtroTipo || tipo.includes(filtroTipo);
        const mostraStatus =
            !filtroStatus ||
            (filtroStatus === 'livre' && status.includes('livre')) ||
            (filtroStatus === 'em-uso' && status.includes('em uso'));

        linha.style.display = (mostraCapacidade && mostraTipo && mostraStatus) ? '' : 'none';
    });
}

// === LIMPAR FILTROS ===
function limparFiltros() {
    document.getElementById('capacidade-filter').value = '0';
    document.getElementById('tipo-filter').value = '';
    document.getElementById('status-filter').value = '';
    filtrarTabela();
}

// === ATUALIZAÇÃO DE HORA E TURNO ===
function atualizarHoraETurno() {
    const horaSpan = document.getElementById("hora-atual");
    const turnoSpan = document.getElementById("turno-atual");

    const agora = new Date();
    const horas = agora.getHours();
    const minutos = agora.getMinutes().toString().padStart(2, '0');
    const segundos = agora.getSeconds().toString().padStart(2, '0');
    const horaFormatada = `${horas.toString().padStart(2, '0')}:${minutos}:${segundos}`;
    if (horaSpan) horaSpan.textContent = horaFormatada;

    let turnoAtual = '';
    if (horas >= 6 && horas < 12) turnoAtual = 'Manhã';
    else if (horas >= 12 && horas < 18) turnoAtual = 'Tarde';
    else turnoAtual = 'Noite';

    if (turnoSpan) turnoSpan.textContent = turnoAtual;
}

// === INICIALIZAÇÃO ===
function inicializarSalas() {
    const capacidadeFilter = document.getElementById('capacidade-filter');
    const tipoFilter = document.getElementById('tipo-filter');
    const statusFilter = document.getElementById('status-filter');

    if (capacidadeFilter) capacidadeFilter.addEventListener('change', filtrarTabela);
    if (tipoFilter) tipoFilter.addEventListener('change', filtrarTabela);
    if (statusFilter) statusFilter.addEventListener('change', filtrarTabela);

    filtrarTabela(); // aplica o filtro logo ao carregar

    atualizarHoraETurno();
    setInterval(atualizarHoraETurno, 1000);
}

// === CHAMAR AO CARREGAR ===
document.addEventListener("DOMContentLoaded", inicializarSalas);

window.salas = {
    inicializar: inicializarSalas,
    filtrarTabela: filtrarTabela,
    limparFiltros: limparFiltros
};

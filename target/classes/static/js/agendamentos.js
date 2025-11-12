function formatarData(dataISO) {
    const data = new Date(dataISO);
    return data.toLocaleDateString('pt-BR');
}

function inicializarAgendamentos() {
    const hoje = new Date().toISOString().split('T')[0];
    const dataAtualElement = document.getElementById('data-atual');

    if (dataAtualElement) {
        dataAtualElement.textContent = formatarData(hoje);
    }

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

    setTimeout(() => {
        window.location.reload();
    }, 120000);
}

document.addEventListener("DOMContentLoaded", () => {
    const horaSpan = document.getElementById("hora-atual");
    const turnoSpan = document.getElementById("turno-atual");

    function atualizarRelogioETurno() {
        const agora = new Date();
        const horas = agora.getHours();
        const minutos = agora.getMinutes().toString().padStart(2, '0');
        const segundos = agora.getSeconds().toString().padStart(2, '0');
        const horaFormatada = `${horas.toString().padStart(2, '0')}:${minutos}:${segundos}`;
        if (horaSpan) horaSpan.textContent = horaFormatada;

        let turnoAtual = '';
        if (horas >= 6 && horas < 12) {
            turnoAtual = 'Manhã';
            turnoSpan.className = "badge fs-6 p-2";
        } else if (horas >= 12 && horas < 18) {
            turnoAtual = 'Tarde';
            turnoSpan.className = "badge fs-6 p-2";
        } else {
            turnoAtual = 'Noite';
            turnoSpan.className = "badge fs-6 p-2";
        }

        if (turnoSpan) turnoSpan.textContent = turnoAtual;
    }

    atualizarRelogioETurno();       // Atualiza ao carregar
    setInterval(atualizarRelogioETurno, 1000); // Atualiza a cada segundo
});

window.agendamentos = {
    inicializar: inicializarAgendamentos,
    formatarData: formatarData
};
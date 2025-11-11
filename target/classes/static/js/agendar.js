// scripts/agendar.js - SEM CONFIRMAÇÕES

let isEditMode = false;

function inicializarModoEdicao() {
    isEditMode = document.getElementById('agendamentoId') && document.getElementById('agendamentoId').value !== '';
    console.log('Modo edição:', isEditMode);
}

function obterDataSelecionada() {
    if (isEditMode) {
        const dataOriginal = document.getElementById("dataOriginal");
        return dataOriginal ? dataOriginal.value : null;
    } else {
        const dataInput = document.getElementById("dataInput");
        return dataInput ? dataInput.value : null;
    }
}

function filtrarSalasPorBloco() {
    const blocoFiltro = document.getElementById('bloco-filter').value;
    const salaSelect = document.getElementById('salaSelect');
    if (!salaSelect) return;

    const options = salaSelect.querySelectorAll('option');
    let salasVisiveis = 0;
    let primeiraSalaValor = '';

    options.forEach(option => {
        if (option.value === '') {
            option.style.display = '';
            option.textContent = blocoFiltro ? `Selecione uma sala do Bloco ${blocoFiltro}...` : 'Selecione uma sala...';
            return;
        }

        const blocoSala = option.dataset.bloco;

        if (!blocoFiltro || blocoFiltro === blocoSala) {
            option.style.display = '';
            salasVisiveis++;
            if (!primeiraSalaValor && option.value) {
                primeiraSalaValor = option.value;
            }
        } else {
            option.style.display = 'none';
        }
    });

    if (salasVisiveis === 1 && primeiraSalaValor && !salaSelect.value) {
        salaSelect.value = primeiraSalaValor;
        atualizarInformacoesSala();
        verificarDisponibilidade();
    }

    if (salaSelect.value) {
        const optionSelecionada = salaSelect.options[salaSelect.selectedIndex];
        if (optionSelecionada.style.display === 'none') {
            salaSelect.value = '';
            atualizarInformacoesSala();
            verificarDisponibilidade();
        }
    }

    atualizarContadorSalas();
}

function atualizarContadorSalas() {
    const blocoFiltro = document.getElementById('bloco-filter').value;
    const salaSelect = document.getElementById('salaSelect');
    if (!salaSelect) return;

    const options = salaSelect.querySelectorAll('option:not([value=""])');
    let salasVisiveis = 0;
    options.forEach(option => {
        if (option.style.display !== 'none') {
            salasVisiveis++;
        }
    });

    const helpText = document.getElementById('bloco-help-text');
    if (helpText) {
        if (blocoFiltro) {
            if (salasVisiveis === 0) {
                helpText.innerHTML = `<span class="text-warning">Nenhuma sala disponível no Bloco ${blocoFiltro}</span>`;
            } else if (salasVisiveis === 1) {
                helpText.innerHTML = `<span class="text-success">1 sala disponível no Bloco ${blocoFiltro} (selecionada automaticamente)</span>`;
            } else {
                helpText.innerHTML = `<span class="text-info">${salasVisiveis} salas disponíveis no Bloco ${blocoFiltro}</span>`;
            }
        } else {
            if (salasVisiveis === 0) {
                helpText.innerHTML = `<span class="text-warning">Nenhuma sala cadastrada no sistema</span>`;
            } else {
                helpText.innerHTML = `<span class="text-info">${salasVisiveis} salas disponíveis em todos os blocos</span>`;
            }
        }
    }
}

function atualizarInformacoesSala() {
    const salaSelect = document.getElementById("salaSelect");
    const salaInfo = document.getElementById("sala-info");
    if (!salaSelect || !salaInfo) return;

    const opt = salaSelect.options[salaSelect.selectedIndex];
    if (!opt.value || !opt.dataset.bloco) {
        salaInfo.textContent = "Selecione uma sala para ver os detalhes";
        return;
    }

    salaInfo.innerHTML = `
        <ul class="list-unstyled mb-0">
            <li><strong>Bloco:</strong> ${opt.dataset.bloco}</li>
            <li><strong>Tipo:</strong> ${opt.dataset.tipo}</li>
            <li><strong>Capacidade:</strong> ${opt.dataset.capacidade} pessoas</li>
        </ul>
    `;
}

function verificarSalaPreSelecionada() {
    const salaSelect = document.getElementById("salaSelect");

    if (salaSelect && salaSelect.value) {
        setTimeout(() => {
            const optionSelecionada = salaSelect.options[salaSelect.selectedIndex];
            const blocoSala = optionSelecionada.dataset.bloco;

            if (blocoSala) {
                const blocoFilter = document.getElementById('bloco-filter');
                if (blocoFilter) {
                    blocoFilter.value = blocoSala;
                    filtrarSalasPorBloco();
                }
            }

            atualizarInformacoesSala();
            if (isEditMode) setTimeout(verificarDisponibilidade, 300);
        }, 100);
    } else {
        setTimeout(filtrarSalasPorBloco, 100);
    }
}

function verificarDisponibilidade() {
    const salaSelect = document.getElementById("salaSelect");
    const data = obterDataSelecionada();
    const turnoSelect = document.getElementById("turnoSelect");
    const disponibilidadeDiv = document.getElementById("disponibilidade");

    if (!salaSelect || !turnoSelect || !disponibilidadeDiv) return;

    const salaId = salaSelect.value;
    const turno = turnoSelect.value;

    if (!salaId || !data || !turno || turno === "0") {
        disponibilidadeDiv.innerHTML = '<span class="text-muted">Selecione sala, data e turno para verificar</span>';
        return;
    }

    disponibilidadeDiv.innerHTML = '<span class="text-info"><i class="bi bi-hourglass-split"></i> Verificando disponibilidade...</span>';

    let url;
    const agendamentoId = document.getElementById("agendamentoId");

    if (isEditMode && agendamentoId && agendamentoId.value) {
        url = `/agendamento/disponibilidade-edicao?salaId=${encodeURIComponent(salaId)}&data=${encodeURIComponent(data)}&turno=${encodeURIComponent(turno)}&agendamentoId=${encodeURIComponent(agendamentoId.value)}`;
    } else {
        url = `/agendamento/disponibilidade?salaId=${encodeURIComponent(salaId)}&data=${encodeURIComponent(data)}&turno=${encodeURIComponent(turno)}`;
    }

    fetch(url)
        .then(r => r.json())
        .then(data => {
            let disponivel = false;
            if (data && typeof data === 'object' && ('disponivel' in data)) {
                disponivel = Boolean(data.disponivel);
            }
            disponibilidadeDiv.innerHTML = disponivel
                ? `<span class="text-success"><i class="bi bi-check-circle-fill"></i> Sala disponível para este turno!</span>`
                : `<span class="text-danger"><i class="bi bi-x-circle-fill"></i> Sala indisponível para este turno</span>`;
        })
        .catch(() => {
            disponibilidadeDiv.innerHTML = `<span class="text-warning"><i class="bi bi-exclamation-triangle-fill"></i> Erro ao verificar disponibilidade</span>`;
        });
}

function inicializarVerificacao() {
    const turnoSelect = document.getElementById("turnoSelect");
    const dataInput = document.getElementById("dataInput");

    if (turnoSelect) turnoSelect.addEventListener("change", verificarDisponibilidade);
    if (dataInput && !isEditMode) dataInput.addEventListener("change", verificarDisponibilidade);
}

function inicializarControlesRepeticao() {
    const btnRepetir = document.getElementById('btn-repetir');
    const btnOcultar = document.getElementById('btn-ocultar-repetir');
    const repeatInput = document.getElementById('repeat-for-input');

    if (btnRepetir && !isEditMode) {
        btnRepetir.addEventListener('click', () => {
            const repetitionOptions = document.getElementById('repetition-options');
            if (repetitionOptions) {
                repetitionOptions.classList.remove('d-none');
                btnRepetir.style.display = 'none';
            }
        });
    }

    if (btnOcultar && !isEditMode) {
        btnOcultar.addEventListener('click', () => {
            const repetitionOptions = document.getElementById('repetition-options');
            if (repetitionOptions && btnRepetir) {
                repetitionOptions.classList.add('d-none');
                btnRepetir.style.display = 'block';
            }
        });
    }

    if (repeatInput && !isEditMode) {
        repeatInput.addEventListener('input', function() {
            const semanas = this.value;
            const diasSpan = document.getElementById('dias-selecionados');
            if (diasSpan) {
                diasSpan.textContent = semanas == 1
                    ? '1 agendamento será criado'
                    : `${semanas} agendamentos serão criados`;
            }
        });
    }
}

function prepararEnvioRecorrente() {
    const salaSelect = document.getElementById("salaSelect");
    const data = obterDataSelecionada();
    const turnoSelect = document.getElementById("turnoSelect");
    const usuarioSelect = document.querySelector("select[name='usuario.id']") || document.querySelector("select[id='usuarioSelect']");

    if (!salaSelect || !salaSelect.value || !data || !turnoSelect || turnoSelect.value === "0" || !usuarioSelect || !usuarioSelect.value) {
        console.log('Campos obrigatórios não preenchidos. Cancelando envio.');
        return false;
    }

    // 🚀 Nenhuma confirmação — envia direto
    console.log("Envio automático do agendamento...");
    return true;
}

function inicializarAgendar() {
    inicializarModoEdicao();

    setTimeout(() => {
        verificarSalaPreSelecionada();
        inicializarVerificacao();
        if (!isEditMode) inicializarControlesRepeticao();
        if (isEditMode) {
            setTimeout(() => {
                if (document.getElementById("salaSelect").value && document.getElementById("turnoSelect").value !== "0") {
                    verificarDisponibilidade();
                }
            }, 800);
        }
    }, 300);
}

function configurarEventListenersAgendar() {
    const blocoFilter = document.getElementById("bloco-filter");
    const salaSelect = document.getElementById("salaSelect");
    const turnoSelect = document.getElementById("turnoSelect");
    const dataInput = document.getElementById("dataInput");

    if (blocoFilter) blocoFilter.addEventListener("change", filtrarSalasPorBloco);
    if (salaSelect) salaSelect.addEventListener("change", () => {
        atualizarInformacoesSala();
        verificarDisponibilidade();
    });
    if (turnoSelect) turnoSelect.addEventListener("change", verificarDisponibilidade);
    if (dataInput && !isEditMode) dataInput.addEventListener("change", verificarDisponibilidade);
}

document.addEventListener('DOMContentLoaded', function() {
    inicializarModoEdicao();
    configurarEventListenersAgendar();
    inicializarAgendar();
});

window.agendar = {
    inicializar: inicializarAgendar,
    configurarEventos: configurarEventListenersAgendar,
    filtrarSalasPorBloco: filtrarSalasPorBloco,
    atualizarInformacoesSala: atualizarInformacoesSala,
    verificarDisponibilidade: verificarDisponibilidade,
    prepararEnvioRecorrente: prepararEnvioRecorrente
};

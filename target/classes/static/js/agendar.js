// scripts/agendar.js

// === VERIFICAR SE É MODO EDIÇÃO ===
let isEditMode = false;

// === INICIALIZAÇÃO DO MODO EDIÇÃO ===
function inicializarModoEdicao() {
    isEditMode = document.getElementById('agendamentoId') && document.getElementById('agendamentoId').value !== '';
}

// === FUNÇÃO PARA OBTER A DATA (FUNCIONA EM AMBOS OS MODOS) ===
function obterDataSelecionada() {
    if (isEditMode) {
        return document.getElementById("dataOriginal").value;
    } else {
        return document.getElementById("dataInput").value;
    }
}

// === FILTRAR SALAS POR BLOCO ===
function filtrarSalasPorBloco() {
    const blocoFiltro = document.getElementById('bloco-filter').value;
    const salaSelect = document.getElementById('salaSelect');
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

// === ATUALIZAR CONTADOR DE SALAS DISPONÍVEIS ===
function atualizarContadorSalas() {
    const blocoFiltro = document.getElementById('bloco-filter').value;
    const salaSelect = document.getElementById('salaSelect');
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

// === MOSTRAR INFORMAÇÕES DA SALA ===
function atualizarInformacoesSala() {
    const salaSelect = document.getElementById("salaSelect");
    const opt = salaSelect.options[salaSelect.selectedIndex];
    const salaInfo = document.getElementById("sala-info");

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

// === VERIFICAR SE HÁ SALA PRÉ-SELECIONADA ===
function verificarSalaPreSelecionada() {
    const salaSelect = document.getElementById("salaSelect");

    if (salaSelect.value) {
        console.log("Sala pré-selecionada detectada:", salaSelect.value);

        setTimeout(() => {
            const optionSelecionada = salaSelect.options[salaSelect.selectedIndex];
            const blocoSala = optionSelecionada.dataset.bloco;

            if (blocoSala) {
                document.getElementById('bloco-filter').value = blocoSala;
                filtrarSalasPorBloco();
            }

            atualizarInformacoesSala();

            if (isEditMode) {
                verificarDisponibilidade();
            }
        }, 100);
    } else {
        setTimeout(() => {
            filtrarSalasPorBloco();
        }, 100);
    }
}

// === INICIALIZAÇÃO DA PÁGINA AGENDAR ===
function inicializarAgendar() {
    inicializarModoEdicao();

    setTimeout(() => {
        verificarSalaPreSelecionada();
        inicializarVerificacao();

        if (isEditMode) {
            console.log("Modo edição detectado - verificações automáticas ativadas");
        }
    }, 300);
}

// === EVENT LISTENERS PARA AGENDAR ===
function configurarEventListenersAgendar() {
    const blocoFilter = document.getElementById("bloco-filter");
    const salaSelect = document.getElementById("salaSelect");

    if (blocoFilter) {
        blocoFilter.addEventListener("change", filtrarSalasPorBloco);
    }

    if (salaSelect) {
        salaSelect.addEventListener("change", function() {
            atualizarInformacoesSala();
            verificarDisponibilidade();
        });
    }
}

// Exportar funções para uso global (se necessário)
window.agendar = {
    inicializar: inicializarAgendar,
    configurarEventos: configurarEventListenersAgendar,
    filtrarSalasPorBloco: filtrarSalasPorBloco,
    atualizarInformacoesSala: atualizarInformacoesSala
};

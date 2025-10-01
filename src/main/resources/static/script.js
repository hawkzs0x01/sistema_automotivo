document.addEventListener('DOMContentLoaded', () => {
    const API_BASE_URL = '/api';

    // Seletores de UI
    const loginView = document.getElementById('login-view');
    const registerView = document.getElementById('register-view');
    const appView = document.getElementById('app-view');
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const vehicleForm = document.getElementById('vehicle-form');
    const showRegisterLink = document.getElementById('show-register');
    const showLoginLink = document.getElementById('show-login');
    const btnLogout = document.getElementById('btn-logout');
    const btnAddVeiculo = document.getElementById('btn-add-veiculo');
    const btnCancel = document.getElementById('btn-cancel');
    const vehicleList = document.getElementById('vehicle-list');
    const vehicleModal = document.getElementById('vehicle-modal');
    const modalTitle = document.getElementById('modal-title');
    const filterMarca = document.getElementById('filter-marca');
    const filterModelo = document.getElementById('filter-modelo');
    const filterAno = document.getElementById('filter-ano');
    const filterPreco = document.getElementById('filter-preco');
    const btnFilter = document.getElementById('btn-filter');
    const btnManageCatalog = document.getElementById('btn-manage-catalog');
    const catalogModal = document.getElementById('catalog-modal');
    const btnCloseCatalogModal = document.getElementById('btn-close-catalog-modal');
    const marcaForm = document.getElementById('marca-form');
    const marcaList = document.getElementById('marca-list');
    const marcaNomeInput = document.getElementById('marca-nome');
    const modeloForm = document.getElementById('modelo-form');
    const modeloList = document.getElementById('modelo-list');
    const modeloNomeInput = document.getElementById('modelo-nome');
    const modeloMarcaSelect = document.getElementById('modelo-marca-select');

    let currentUser = null;

    // --- FUNÇÕES AUXILIARES ---
    const showView = (viewId) => {
        loginView.classList.add('hidden');
        registerView.classList.add('hidden');
        appView.classList.add('hidden');
        document.getElementById(viewId).classList.remove('hidden');
    };

    const apiFetch = async (endpoint, options = {}) => {
        options.credentials = 'include';
        try {
            const response = await fetch(API_BASE_URL + endpoint, options);
            const responseText = await response.text();
            if (!response.ok) {
                let errorMsg = responseText;
                try {
                    const errorJson = JSON.parse(responseText);
                    errorMsg = Object.values(errorJson).join(', ');
                } catch (e) { /* Não é JSON, usa o texto direto */ }
                throw new Error(errorMsg || `Erro ${response.status}`);
            }
            if (response.status === 204 || responseText === "") return null;
            try { return JSON.parse(responseText); } catch (e) { return responseText; }
        } catch (error) {
            console.error(`Falha na API para ${endpoint}:`, error);
            alert(`Erro: ${error.message}`);
            return null;
        }
    };

    const updateUIForRole = () => {
        const isAdmin = currentUser && currentUser.role === 'ROLE_ADMIN';
        document.querySelector('.admin-buttons').style.display = isAdmin ? 'flex' : 'none';
        document.querySelectorAll('.admin-actions').forEach(actions => {
            actions.style.display = isAdmin ? 'flex' : 'none';
        });
        document.querySelectorAll('.user-actions').forEach(actions => {
            actions.style.display = !isAdmin ? 'flex' : 'none';
        });
    };

    const populateSelect = async (selectElement, endpoint, nameField, valueField, prompt) => {
        const data = await apiFetch(endpoint);
        selectElement.innerHTML = `<option value="">${prompt}</option>`;
        if (data && Array.isArray(data)) {
            data.forEach(item => selectElement.add(new Option(item[nameField], item[valueField])));
        }
    };

    // --- RENDERIZAÇÃO ---
    const renderVeiculos = (veiculos) => {
        vehicleList.innerHTML = '';
        if (!veiculos || !Array.isArray(veiculos) || veiculos.length === 0) {
            vehicleList.innerHTML = '<p>Nenhum veículo encontrado para os filtros selecionados.</p>';
            return;
        }
        veiculos.forEach(v => {
            const card = document.createElement('div');
            card.className = 'vehicle-card';
            const carAlt = (v.modelo && v.modelo.marca) ? `${v.modelo.marca.nome} ${v.modelo.nome}` : 'Veículo';

            // ===== AQUI ESTÁ A CORREÇÃO PARA USAR A IMAGEM LOCAL =====
            card.innerHTML = `
                <img src="${v.urlCapa}" class="vehicle-image" alt="${carAlt}" onerror="this.onerror=null;this.src='https://placehold.co/600x400/ccc/fff?text=Imagem+N%C3%A3o+Encontrada';">
                <div class="vehicle-card-content">
                    <h3>${carAlt}</h3>
                    <p><strong>Ano:</strong> ${v.anoFabricacao}</p>
                    <p><strong>Cor:</strong> ${v.cor || 'Não informada'}</p>
                    <p><strong>KM:</strong> ${v.quilometragem != null ? v.quilometragem.toLocaleString('pt-BR') : '0'}</p>
                    <p class="price">R$ ${v.preco.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</p>
                    <p><strong>Status:</strong> <span style="color: ${v.disponivel ? 'var(--success-color)' : 'var(--danger-color)'}">${v.disponivel ? 'Disponível' : 'Vendido'}</span></p>
                </div>
                <div class="vehicle-card-actions admin-actions">
                    <button class="btn btn-edit" data-id="${v.id}">Editar</button>
                    <button class="btn btn-delete" data-id="${v.id}">Excluir</button>
                </div>
                <div class="vehicle-card-actions user-actions">
                    <button class="btn btn-buy" data-id="${v.id}" ${!v.disponivel ? 'disabled' : ''}>${v.disponivel ? 'Tenho Interesse' : 'Vendido'}</button>
                </div>`;
            vehicleList.appendChild(card);
        });
        updateUIForRole();
    };

    const loadVeiculos = async () => {
        vehicleList.innerHTML = '<p>Carregando veículos...</p>';
        const params = new URLSearchParams();
        if (filterMarca.value) params.append('marcaId', filterMarca.value);
        if (filterModelo.value) params.append('modeloId', filterModelo.value);
        if (filterAno.value) params.append('ano', filterAno.value);
        if (filterPreco.value) params.append('precoMax', filterPreco.value);
        const veiculos = await apiFetch(`/veiculos?${params.toString()}`);
        renderVeiculos(veiculos);
    };

    const openModal = async (veiculo = null) => {
        vehicleForm.reset();
        document.getElementById('veiculo-id').value = '';
        const formMarca = document.getElementById('form-marca');
        const formModelo = document.getElementById('form-modelo');
        await populateSelect(formMarca, '/marcas', 'nome', 'id', 'Selecione uma Marca');
        await populateSelect(formModelo, '/modelos', 'nome', 'id', 'Selecione um Modelo');
        if (veiculo) {
            modalTitle.textContent = 'Editar Veículo';
            document.getElementById('veiculo-id').value = veiculo.id;
            setTimeout(() => {
                formMarca.value = veiculo.modelo.marca.id;
                formModelo.value = veiculo.modelo.id;
            }, 200);
            document.getElementById('form-ano').value = veiculo.anoFabricacao;
            document.getElementById('form-preco').value = veiculo.preco;
            document.getElementById('form-km').value = veiculo.quilometragem;
            document.getElementById('form-cor').value = veiculo.cor;
            document.getElementById('form-url-capa').value = veiculo.urlCapa;
            document.getElementById('form-disponivel').checked = veiculo.disponivel;
        } else {
            modalTitle.textContent = 'Adicionar Novo Veículo';
        }
        vehicleModal.classList.remove('hidden');
    };

    const renderMarcasAdmin = (marcas) => {
        marcaList.innerHTML = '';
        if (marcas) marcas.forEach(marca => {
            const li = document.createElement('li');
            li.className = 'catalog-list-item';
            li.innerHTML = `<span>${marca.nome}</span><div><button class="btn-edit-catalog" data-id="${marca.id}" data-name="${marca.nome}">✏️</button><button class="btn-delete-catalog" data-id="${marca.id}">🗑️</button></div>`;
            marcaList.appendChild(li);
        });
    };

    const loadCatalogAdmin = async () => {
        const marcas = await apiFetch('/marcas');
        renderMarcasAdmin(marcas);
        populateSelect(modeloMarcaSelect, '/marcas', 'nome', 'id', 'Selecione a Marca');
        // A lógica de renderizar modelos no admin pode ser adicionada aqui no futuro
    };

    // --- EVENT LISTENERS ---
    showRegisterLink.addEventListener('click', (e) => { e.preventDefault(); showView('register-view'); });
    showLoginLink.addEventListener('click', (e) => { e.preventDefault(); showView('login-view'); });
    btnLogout.addEventListener('click', () => {
        currentUser = null;
        sessionStorage.removeItem('currentUser');
        showView('login-view');
    });
    btnFilter.addEventListener('click', loadVeiculos);
    btnAddVeiculo.addEventListener('click', () => openModal());
    btnCancel.addEventListener('click', () => vehicleModal.classList.add('hidden'));
    btnManageCatalog.addEventListener('click', () => { catalogModal.classList.remove('hidden'); loadCatalogAdmin(); });
    btnCloseCatalogModal.addEventListener('click', () => catalogModal.classList.add('hidden'));

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        try {
            const user = await apiFetch('/auth/login', {
                method: 'POST', headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: loginForm['login-username'].value, password: loginForm['login-password'].value })
            });
            if (user && typeof user === 'object') {
                currentUser = user;
                sessionStorage.setItem('currentUser', JSON.stringify(user));
                document.getElementById('user-greeting').textContent = `Olá, ${currentUser.username}!`;
                showView('app-view');
                loadInitialData();
            } else { throw new Error('Resposta de login inválida.'); }
        } catch (error) {
            document.getElementById('login-error').textContent = "Usuário ou senha inválidos.";
        }
    });

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const result = await apiFetch('/auth/register', {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: registerForm['register-username'].value,
                telefone: registerForm['register-telefone'].value,
                password: registerForm['register-password'].value
            })
        });
        if (result) {
            alert('Usuário registrado com sucesso! Por favor, faça o login.');
            showView('login-view');
        }
    });

    vehicleForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = document.getElementById('veiculo-id').value;
        const dto = {
            modeloId: document.getElementById('form-modelo').value,
            anoFabricacao: document.getElementById('form-ano').value,
            preco: document.getElementById('form-preco').value,
            quilometragem: document.getElementById('form-km').value,
            cor: document.getElementById('form-cor').value,
            disponivel: document.getElementById('form-disponivel').checked,
            urlCapa: document.getElementById('form-url-capa').value,
        };
        const result = await apiFetch(`/veiculos${id ? `/${id}` : ''}`, {
            method: id ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dto)
        });
        if (result !== null) {
            vehicleModal.classList.add('hidden');
            loadVeiculos();
        }
    });

    vehicleList.addEventListener('click', async (e) => {
        const id = e.target.dataset.id;
        if (e.target.classList.contains('btn-edit')) {
            const veiculo = await apiFetch(`/veiculos/${id}`);
            if (veiculo) openModal(veiculo);
        }
        if (e.target.classList.contains('btn-delete')) {
            if (confirm('Atenção: Esta ação irá excluir permanentemente o registro. Deseja continuar?')) {
                await apiFetch(`/veiculos/${id}`, { method: 'DELETE' });
                loadVeiculos();
            }
        }
        if (e.target.classList.contains('btn-buy')) {
            if (confirm('Deseja registrar seu interesse neste veículo? Um vendedor entrará em contato.')) {
                const result = await apiFetch(`/veiculos/${id}/vender`, { method: 'POST' });
                if (result) {
                    alert('Interesse registrado com sucesso! Um vendedor entrará em contato com você em breve pelo número cadastrado.');
                    loadVeiculos();
                }
            }
        }
    });

    marcaForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const nome = marcaNomeInput.value;
        if (!nome) return;
        const result = await apiFetch('/marcas', {
            method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome })
        });
        if (result) {
            marcaNomeInput.value = '';
            loadCatalogAdmin();
            populateSelect(filterMarca, '/marcas', 'nome', 'id', 'Todas as Marcas');
        }
    });

    marcaList.addEventListener('click', async (e) => {
        const id = e.target.dataset.id;
        if (e.target.classList.contains('btn-edit-catalog')) {
            const currentName = e.target.dataset.name;
            const newName = prompt('Digite o novo nome para a marca:', currentName);
            if (newName && newName.trim() !== '' && newName !== currentName) {
                const result = await apiFetch(`/marcas/${id}`, {
                    method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ nome: newName })
                });
                if (result) {
                    loadCatalogAdmin();
                    populateSelect(filterMarca, '/marcas', 'nome', 'id', 'Todas as Marcas');
                    loadVeiculos(); // Atualiza a lista principal caso um nome de marca tenha mudado
                }
            }
        }
        if (e.target.classList.contains('btn-delete-catalog')) {
            if (confirm('Atenção: Excluir uma marca também excluirá todos os modelos e veículos associados. Deseja continuar?')) {
                await apiFetch(`/marcas/${id}`, { method: 'DELETE' });
                loadCatalogAdmin();
                populateSelect(filterMarca, '/marcas', 'nome', 'id', 'Todas as Marcas');
                loadVeiculos();
            }
        }
    });

    // --- INICIALIZAÇÃO ---
    const loadInitialData = () => {
        populateSelect(filterMarca, '/marcas', 'nome', 'id', 'Todas as Marcas');
        populateSelect(filterModelo, '/modelos', 'nome', 'id', 'Todos os Modelos');
        loadVeiculos();
    };

    const checkSession = () => {
        const user = sessionStorage.getItem('currentUser');
        if (user) {
            currentUser = JSON.parse(user);
            document.getElementById('user-greeting').textContent = `Olá, ${currentUser.username}!`;
            showView('app-view');
            loadInitialData();
        } else {
            showView('login-view');
        }
    };

    checkSession();
});
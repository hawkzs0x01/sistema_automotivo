-- Este script insere apenas os dados do catálogo.
-- Os usuários de teste (admin, user) são criados pelo DataInitializer.java.

-- Limpa os dados de veículos, modelos e marcas para garantir um início limpo.
DELETE FROM veiculo;
DELETE FROM modelo;
DELETE FROM marca;
DELETE FROM usuario;

-- Inserindo Marcas
INSERT INTO marca (nome) VALUES ('Ford'), ('Chevrolet'), ('Toyota'), ('Honda'), ('Volkswagen'), ('BMW'), ('Audi'), ('Hyundai'), ('Jeep');

-- Inserindo Modelos (IDs das marcas começam em 1)
INSERT INTO modelo (nome, marca_id) VALUES
('Mustang', 1), ('Focus', 1), ('Ka', 1),
('Onix', 2), ('Cruze', 2), ('Tracker', 2),
('Corolla', 3), ('Hilux', 3), ('Yaris', 3),
('Civic', 4), ('HR-V', 4),
('Golf', 5), ('Polo', 5), ('T-Cross', 5),
('Série 3', 6), ('X1', 6),
('A3', 7), ('Q3', 7),
('HB20', 8), ('Creta', 8),
('Renegade', 9), ('Compass', 9);

-- Inserindo Veículos com CAMINHOS LOCAIS para as imagens
INSERT INTO veiculo (ano_fabricacao, cor, preco, quilometragem, disponivel, modelo_id, url_capa) VALUES
(2024, 'Preto', 350000.00, 100, true, 1, '/images/ford-mustang.jpg'),
(2022, 'Azul', 85000.00, 30000, true, 4, '/images/chevrolet-onix.jpg'),
(2023, 'Branco', 150000.00, 15000, true, 7, '/images/toyota-corolla.jpg'),
(2020, 'Branco', 95000.00, 50000, true, 10, '/images/honda-civic.jpg'),
(2021, 'Branco', 115000.00, 45000, true, 12, '/images/vw-golf.jpg'),
(2023, 'Cinza', 130000.00, 22000, true, 14, '/images/vw-tcross.jpg'),
(2024, 'Cinza', 280000.00, 500, true, 15, '/images/bmw-serie3.jpg'),
(2022, 'Cinza', 180000.00, 18000, false, 16, '/images/bmw-x1.jpg'),
(2023, 'Cinza', 75000.00, 25000, true, 19, '/images/hyundai-hb20.jpg'),
(2024, 'Preto', 145000.00, 1000, true, 20, '/images/hyundai-creta.jpg'),
(2022, 'Laranja', 125000.00, 35000, true, 21, '/images/jeep-renegade.jpg'),
(2023, 'Chumbo', 170000.00, 12000, false, 22, '/images/jeep-compass.jpg');
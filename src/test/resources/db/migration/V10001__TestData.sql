INSERT INTO tariffs (id, name, start_date, end_date, product, version, state) VALUES
('548ea2e0-bcef-4e12-b933-803a4de50106', 'Tariff 1 Create', '2020-01-01T12:00:00.000', '2020-01-01T13:00:00.000', null, 0, 'INACTIVE'),
('548ea2e0-bcef-4e12-b933-803a4de50106', 'Tariff 1 Update 1', '2020-01-01T13:00:00.000', '2020-01-01T14:00:00.000', '284add3b-e6f2-45f6-8a5e-1dfbed6a1f40', 1, 'INACTIVE'),
('548ea2e0-bcef-4e12-b933-803a4de50106', 'Tariff 1 Update 2', '2020-01-01T14:00:00.000', null, '5c50cc6c-8600-48a3-acf8-a83298035857', 2, 'ACTIVE');

INSERT INTO tariffs (id, name, start_date, end_date, product, version, state) VALUES
('f13893a4-7951-4c69-8c77-d292ddb40737', 'Tariff 2 Create', '2020-01-01T12:00:00.000', '2020-01-01T14:00:00.000', '5c50cc6c-8600-48a3-acf8-a83298035857', 0, 'INACTIVE'),
('f13893a4-7951-4c69-8c77-d292ddb40737', 'Tariff 2 Update 1', '2020-01-01T14:00:00.000', null, null, 1, 'ACTIVE');

INSERT INTO tariffs (id, name, start_date, end_date, product, version, state) VALUES
('15cfb4c1-7083-475e-838d-4a1e696cf917', 'Tariff Del Create', '2020-01-01T12:00:00.000', '2020-01-01T14:00:00.000', null, 0, 'INACTIVE'),
('15cfb4c1-7083-475e-838d-4a1e696cf917', 'Tariff Del Update 1', '2020-01-01T14:00:00.000', null, '8c123176-bca5-4f75-9a70-a7cd8db5cb00', 1, 'ACTIVE');
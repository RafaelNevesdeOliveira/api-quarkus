-- Carga inteiramente sintética para leitura didática em uma instância isolada.
-- A carga original foi excluída para não transportar hash ou credenciais.
INSERT INTO public.tipos_conta (nome) VALUES ('CORRENTE'), ('POUPANCA')
ON CONFLICT (nome) DO NOTHING;
INSERT INTO public.pessoas (nome, cpf, email)
VALUES ('Pessoa Exemplo', '12345678901', 'pessoa@example.test')
ON CONFLICT (cpf) DO NOTHING;
INSERT INTO public.contas_bancarias
    (agencia, numero, saldo, ativa, titular_id, tipo_conta_id)
SELECT '0001', '100001-1', 100.00, TRUE, p.id, t.id
FROM public.pessoas p, public.tipos_conta t
WHERE p.cpf = '12345678901' AND t.nome = 'CORRENTE'
ON CONFLICT (agencia, numero) DO NOTHING;

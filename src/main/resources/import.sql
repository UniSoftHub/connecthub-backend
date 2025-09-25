-- Ordem de inserção respeitando as dependências entre tabelas
-- 1. USERS (base para outras entidades)
INSERT INTO users (id, role, name, email, password, xp, level, avatar_url, is_active, created_at, updated_at) VALUES
(1, 'STUDENT', 'João Silva', 'joao.silva@estudante.edu.br', '$2a$10$hashedpassword1', 150.0, 2, 'https://avatar.com/joao', true, '2024-01-15 08:00:00', '2024-01-15 08:00:00'),
(2, 'TEACHER', 'Prof. Maria Santos', 'maria.santos@professor.edu.br', '$2a$10$hashedpassword2', 500.0, 5, 'https://avatar.com/maria', true, '2024-01-10 09:00:00', '2024-01-10 09:00:00'),
(3, 'STUDENT', 'Carlos Oliveira', 'carlos.oliveira@estudante.edu.br', '$2a$10$hashedpassword3', 75.0, 1, 'https://avatar.com/carlos', true, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(4, 'STUDENT', 'Ana Costa', 'ana.costa@estudante.edu.br', '$2a$10$hashedpassword4', 300.0, 3, 'https://avatar.com/ana', true, '2024-01-12 11:00:00', '2024-01-12 11:00:00'),
(5, 'ADMIN', 'Admin Sistema', 'admin@sistema.edu.br', '$2a$10$hashedpassword5', 1000.0, 10, 'https://avatar.com/admin', true, '2024-01-01 12:00:00', '2024-01-01 12:00:00');

-- 2. COURSES
INSERT INTO courses (id, name, code, semester, workload, is_active, created_at, updated_at) VALUES
(1, 'Programação Orientada a Objetos', 'POO001', 3, 80, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(2, 'Estruturas de Dados', 'ED002', 4, 60, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(3, 'Banco de Dados', 'BD003', 5, 100, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(4, 'Desenvolvimento Web', 'WEB004', 6, 120, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(5, 'Inteligência Artificial', 'IA005', 8, 90, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00');

-- 3. TOPICS 
INSERT INTO topics (id, title, content, course_id, author_id, topic_status, count_views, is_active, created_at, updated_at) VALUES
(1, 'Dúvida sobre Herança em Java', 'Estou com dificuldades para entender como funciona a herança em Java. Alguém pode me explicar com exemplos práticos?', 1, 1, 'NOT_ANSWERED', 25, true, '2024-02-01 09:00:00', '2024-02-01 09:00:00'),
(2, 'Implementação de Lista Ligada', 'Preciso de ajuda para implementar uma lista ligada em C++. Qual seria a melhor abordagem?', 2, 3, 'NOT_SOLVED', 15, true, '2024-02-05 10:30:00', '2024-02-05 10:30:00'),
(3, 'Normalização de Banco de Dados', 'Como aplicar corretamente a terceira forma normal em um banco de dados?', 3, 4, 'SOLVED', 40, true, '2024-02-10 14:00:00', '2024-02-10 14:00:00'),
(4, 'React vs Vue.js', 'Qual framework JavaScript vocês recomendam para iniciantes? React ou Vue.js?', 4, 1, 'NOT_SOLVED', 60, true, '2024-02-15 16:20:00', '2024-02-15 16:20:00'),
(5, 'Algoritmos de Machine Learning', 'Quais são os algoritmos mais importantes para começar a estudar ML?', 5, 4, 'NOT_ANSWERED', 35, true, '2024-02-20 11:45:00', '2024-02-20 11:45:00');

-- 4. ANSWERS
INSERT INTO answers (id, content, topic_id, author_id, count_likes, count_dislikes, is_solution, is_active, created_at, updated_at) VALUES
(1, 'Herança é um dos pilares da POO. Basicamente permite que uma classe filha herde atributos e métodos de uma classe pai. Por exemplo: class Pessoa { String nome; } class Estudante extends Pessoa { String curso; }', 1, 2, 5, 0, false, true, '2024-02-01 10:00:00', '2024-02-01 10:00:00'),
(2, 'Para lista ligada, você precisa criar uma estrutura Node com dados e ponteiro para o próximo. Depois implementar métodos insert, delete e search. Posso compartilhar um código exemplo se quiser.', 2, 2, 8, 0, true, true, '2024-02-05 11:00:00', '2024-02-05 11:00:00'),
(3, 'A 3FN elimina dependências transitivas. Se A->B e B->C, então C deve estar em outra tabela. Exemplo: se Estudante->Curso e Curso->Departamento, então Departamento deve ter sua própria tabela.', 3, 2, 12, 1, true, true, '2024-02-10 15:00:00', '2024-02-10 15:00:00'),
(4, 'Para iniciantes, recomendo React. Tem mais documentação, comunidade maior e mais oportunidades de trabalho. Vue é mais fácil de aprender, mas React te dará uma base sólida.', 4, 4, 15, 2, false, true, '2024-02-15 17:00:00', '2024-02-15 17:00:00'),
(5, 'Comece com regressão linear, árvores de decisão e k-means. São algoritmos fundamentais e relativamente simples de entender. Depois parta para redes neurais.', 5, 2, 3, 0, false, true, '2024-02-20 12:30:00', '2024-02-20 12:30:00');

-- 5. BADGES
INSERT INTO badges (id, name, description, image_url, criteria, is_active, created_at, updated_at) VALUES
(1, 'Primeiro Post', 'Parabéns por fazer sua primeira pergunta na plataforma!', 'https://badges.com/first-post.png', 'Criar o primeiro tópico', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(2, 'Solucionador', 'Você ajudou outros estudantes com suas respostas!', 'https://badges.com/solver.png', 'Ter 5 respostas marcadas como solução', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(3, 'Expert', 'Reconhecido pela comunidade como especialista na área', 'https://badges.com/expert.png', 'Atingir 500 pontos de XP', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(4, 'Colaborativo', 'Ativo na comunidade, sempre disposto a ajudar', 'https://badges.com/collaborative.png', 'Responder 20 perguntas', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(5, 'Mentor', 'Guia e orienta novos estudantes na plataforma', 'https://badges.com/mentor.png', 'Atingir nível 5 e ter 10 soluções aceitas', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00');

-- 6. USER_BADGES
INSERT INTO user_badges (id, user_id, badge_id, earned_at, is_active, created_at, updated_at) VALUES
(1, 1, 1, '2024-02-01 09:05:00', true, '2024-02-01 09:05:00', '2024-02-01 09:05:00'),
(2, 2, 3, '2024-01-15 14:00:00', true, '2024-01-15 14:00:00', '2024-01-15 14:00:00'),
(3, 2, 5, '2024-01-20 16:30:00', true, '2024-01-20 16:30:00', '2024-01-20 16:30:00'),
(4, 4, 1, '2024-02-10 14:05:00', true, '2024-02-10 14:05:00', '2024-02-10 14:05:00'),
(5, 4, 2, '2024-02-15 18:00:00', true, '2024-02-15 18:00:00', '2024-02-15 18:00:00');

-- 7. XP_TRANSACTIONS 
INSERT INTO xp_transactions (id, user_id, amount, description, reference_id, type, is_active, created_at, updated_at) VALUES
(1, 1, 10, 'Primeira pergunta postada', 1, 'TOPIC_CREATED', true, '2024-02-01 09:00:00', '2024-02-01 09:00:00'),
(2, 2, 15, 'Resposta útil postada', 1, 'ANSWER_POSTED', true, '2024-02-01 10:30:00', '2024-02-01 10:30:00'),
(3, 3, 10, 'Nova pergunta sobre estruturas de dados', 2, 'TOPIC_CREATED', true, '2024-02-05 10:30:00', '2024-02-05 10:30:00'),
(4, 2, 25, 'Resposta marcada como melhor resposta', 3, 'BEST_ANSWER', true, '2024-02-05 11:30:00', '2024-02-05 11:30:00'),
(5, 1, 20, 'Projeto publicado', 1, 'PROJECT_PUBLISHED', true, '2024-01-25 14:30:00', '2024-01-25 14:30:00');

-- 8. NOTIFICATIONS 
INSERT INTO notifications (id, user_id, type, title, message, reference_id, is_read, is_active, created_at, updated_at) VALUES
(1, 1, 'TOPIC_ANSWERED', 'Nova resposta!', 'Sua pergunta sobre herança em Java recebeu uma resposta.', 1, false, true, '2024-02-01 10:05:00', '2024-02-01 10:05:00'),
(2, 2, 'BEST_ANSWER_SELECTED', 'Resposta selecionada!', 'Sua resposta foi escolhida como a melhor resposta.', 3, true, true, '2024-02-10 16:00:00', '2024-02-10 16:00:00'),
(3, 3, 'TOPIC_ANSWERED', 'Resposta na sua pergunta', 'Alguém respondeu sua dúvida sobre lista ligada.', 2, false, true, '2024-02-05 11:05:00', '2024-02-05 11:05:00'),
(4, 4, 'BADGE_EARNED', 'Nova conquista!', 'Você ganhou uma nova medalha!', 2, false, true, '2024-02-15 18:05:00', '2024-02-15 18:05:00'),
(5, 1, 'LEVEL_UP', 'Parabéns!', 'Você subiu de nível!', 1, true, true, '2024-02-16 09:00:00', '2024-02-16 09:00:00');

-- 9. PROJECTS
INSERT INTO projects (id, name, description, author_id, repository_url, image_url, count_views, is_active, created_at, updated_at) VALUES
(1, 'Sistema de Biblioteca', 'Sistema completo para gerenciamento de biblioteca universitária com empréstimos e devoluções', 1, 'https://github.com/joao/biblioteca-sistema', 'https://images.com/biblioteca.png', 120, true, '2024-01-25 14:00:00', '2024-01-25 14:00:00'),
(2, 'E-commerce Responsivo', 'Loja virtual desenvolvida com React e Node.js, incluindo carrinho e pagamentos', 4, 'https://github.com/ana/ecommerce-app', 'https://images.com/ecommerce.png', 200, true, '2024-02-01 16:00:00', '2024-02-01 16:00:00'),
(3, 'API de Gerenciamento de Tarefas', 'REST API para aplicativo de produtividade com autenticação JWT', 3, 'https://github.com/carlos/task-api', 'https://images.com/tasks.png', 85, true, '2024-02-10 10:00:00', '2024-02-10 10:00:00'),
(4, 'Dashboard Analytics', 'Dashboard interativo para visualização de dados com gráficos e relatórios', 2, 'https://github.com/maria/analytics-dashboard', 'https://images.com/dashboard.png', 150, true, '2024-02-15 09:00:00', '2024-02-15 09:00:00'),
(5, 'App Mobile Fitness', 'Aplicativo React Native para acompanhamento de exercícios e dieta', 1, 'https://github.com/joao/fitness-app', 'https://images.com/fitness.png', 90, true, '2024-02-18 11:00:00', '2024-02-18 11:00:00');

-- 10. PROJECT_TECHNOLOGIES 
INSERT INTO project_technologies (project_id, technology) VALUES
(1, 'JAVA'),
(1, 'SPRING'),
(1, 'MYSQL'),
(2, 'REACT'),
(2, 'NODEJS'),
(2, 'MONGODB'),
(3, 'PYTHON'),
(3, 'FLASK'),
(3, 'POSTGRESQL'),
(4, 'JAVASCRIPT'),
(4, 'VUE'),
(4, 'CSS'),
(5, 'REACT'),
(5, 'JAVASCRIPT'),
(5, 'NODEJS');

-- 11. PROJECT_COMMENTS
INSERT INTO project_comments (id, text, project_id, author_id, is_active, created_at, updated_at) VALUES
(1, 'Projeto muito bem estruturado! O código está bem organizado e documentado. Parabéns!', 1, 4, true, '2024-01-26 15:00:00', '2024-01-26 15:00:00'),
(2, 'Gostei muito da interface do e-commerce. Como você implementou o sistema de pagamentos?', 2, 3, true, '2024-02-02 17:30:00', '2024-02-02 17:30:00'),
(3, 'A API está muito bem documentada. Seria possível adicionar testes unitários?', 3, 2, true, '2024-02-11 11:30:00', '2024-02-11 11:30:00'),
(4, 'Dashboard ficou incrível! Os gráficos estão muito informativos. Qual biblioteca usou?', 4, 1, true, '2024-02-16 10:15:00', '2024-02-16 10:15:00'),
(5, 'App de fitness tem um design muito clean. Pretende adicionar integração com wearables?', 5, 4, true, '2024-02-19 12:45:00', '2024-02-19 12:45:00');


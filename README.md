## Introdução

O **MyTasks** é um aplicativo simples de gerenciamento de tarefas que tem como objetivo explorar e demonstrar recursos úteis do desenvolvimento Android. Ele utiliza o padrão de arquitetura **MVVM** (Model-View-ViewModel) para garantir uma estrutura modular, testável e de fácil manutenção. Este documento aborda a camada de dados, explicando suas responsabilidades, classes e interfaces.

## Módulo `data`

O módulo `data` é responsável por gerenciar e fornecer acesso aos dados persistentes da aplicação. Ele utiliza o **Room** como biblioteca ORM para interagir com o banco de dados SQLite.

### Camada de Banco de Dados

#### TaskEntity

A classe `TaskEntity` representa a entidade de tarefa no banco de dados. 

- **id**: Identificador único da tarefa.
- **title**: Título da tarefa.
- **description**: Descrição opcional da tarefa.
- **isCompleted**: Indica se a tarefa foi concluída.

#### MyTasksDatabase

O `MyTasksDatabase` é a classe de banco de dados que define a configuração da base de dados e fornece acesso ao DAO.

- **@Database**: Annotation que define as entidades e a versão do banco de dados.
- **taskDao()**: Método abstrato que retorna a instância de `TaskDao`.

### DAO (Data Access Object)

O `TaskDao` define as operações de acesso ao banco de dados para a entidade `TaskEntity`.

- **findAllTasks()**: Retorna todas as tarefas como um fluxo de dados.
- **findTaskById(id: String)**: Busca uma tarefa pelo seu ID.
- **save(task: TaskEntity)**: Insere ou atualiza uma tarefa.
- **delete(task: TaskEntity)**: Remove uma tarefa.

### Repositório

O `TaskRepository` age como uma ponte entre a camada de dados e as ViewModels, garantindo que a lógica de acesso aos dados esteja isolada.

- **tasks**: Fluxo de todas as tarefas armazenadas.
- **insertTask(task: Task)**: Insere uma nova tarefa no banco de dados.
- **toggleIsCompleted(task: Task)**: Alterna o estado de conclusão de uma tarefa.
- **delete(id: String)**: Remove uma tarefa com base no ID.
- **findTaskById(id: String)**: Retorna uma tarefa pelo ID.

### Conversões

As funções de extensão realizam a conversão entre as classes `Task` e `TaskEntity`, facilitando a manipulação dos dados entre as camadas da aplicação.

- **toTaskEntity()**: Converte um `Task` em um `TaskEntity`.
- **toTask()**: Converte um `TaskEntity` em um `Task`.

## Módulo `ui`

#### MainActivity.kt

A `MainActivity` é o ponto de entrada da aplicação e responsável por configurar a navegação entre as telas utilizando o Jetpack Navigation e o Material Design 3. A lógica principal está em:

- **NavHost**: Gerencia as rotas e define o fluxo de navegação.
- **taskListFragment**: Configura a navegação para a lista de tarefas.
- **taskFormScreen**: Configura a navegação para o formulário de tarefas.

#### MyTasksApplication.kt

A `MyTasksApplication` inicializa o Koin como provedor de dependências para toda a aplicação. Sua configuração principal está em:

- **startKoin**: Configura o Koin para gerenciar as dependências do projeto.
- **modules**: Define os módulos de injeção de dependência, como `appModule` e `storageModule`.

#### TasksListScreen.kt

A tela inicial do aplicativo, onde as tarefas salvas são exibidas. Caso nenhuma tarefa tenha sido adicionada, uma mensagem de aviso é mostrada. Permite navegar para:
- Adicionar uma nova tarefa.
- Editar uma tarefa existente.

#### TaskFormRoute.kt

Responsável por definir a rota para o formulário de tarefas. Implementa o método `taskFormScreen`, que configura a navegação para a tela de edição/criação de tarefas.

#### TasksListRoute.kt

Define a rota para a lista de tarefas. Contém o método `taskListFragment`, que configura a navegação para a tela de listagem de tarefas e recebe os callbacks para navegar para a tela de criação ou edição de tarefas.

#### TaskFormScreen.kt

Componente composable que representa a tela do formulário de tarefas. Possui integração com o `TaskFormViewModel` para gerenciar o estado da tela e manipular eventos como salvar ou excluir uma tarefa.

#### TaskFormBody.kt

Componente composable que define a estrutura visual do formulário de tarefas, incluindo campos de entrada para título e descrição, além de botões flutuantes para salvar ou excluir a tarefa. Utiliza o Material Design 3.

### ViewModels

##### TaskListViewModel

Gerencia o estado da tela de listagem de tarefas.

- **repository**: Fornece os dados para exibir na lista de tarefas.
- **onTaskDoneChange**: Alterna o estado de conclusão da tarefa.

##### TaskFormViewModel

Gerencia o estado e os eventos da tela de formulário de tarefas.

- **id**: Identificador da tarefa, utilizado para editar ou criar uma nova.
- **onSaveClick**: Salva a tarefa.
- **onDeleteClick**: Exclui a tarefa se houver um ID associado.

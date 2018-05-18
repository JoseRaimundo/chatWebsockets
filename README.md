# Mini-Projeto II – Chat Multi-salas
## Descrição

Desenvolva uma aplicação que permita um chat, em grupo, com possíveis salas. Não há limites para o número de participantes em cada sala, nem do numero de salas. No entanto, um usuário só pode participar de uma sala, por vez. 
No chat, a qualquer momento um usuário poderá enviar/receber mensagens ou ate mesmo renomear seu nome. A mensagem poderá ser enviada a sala (permitindo que todos os participantes a vejam) ou, reservadamente, a um usuário específico, que esteja na mesma sala. Se usuário solicitar sua renomeação (informando o novo nome desejado), o sistema devera informar ao usuário que seu nome foi alterado com sucesso, caso o novo nome não exista na sala. Alem disso, o sistema devera informar a todos a alteração que ocorreu, informando o nome anterior e o atual. 
Cada mensagem aparecerá na sala, para os usuários, no seguinte formato (exceto quando não for uma mensagem reservada):

		[usuario] [hora] : [mensagem] 
**Onde:**

 -  [usuário] – nome do usuário que enviou a mensagem 
 -  [hora] – hora que foi enviada a mensagem (considerar hora no servidor) 
 -  [mensagem] – a mensagem enviada pelo usuário.

 No caso de uma mensagem reservada, a mensagem devera ser exibida, apenas para o usuário desejado, no formato: 
		
		[usuario] [hora] reservadamente: [mensagem]

Para se conectar a uma sala, o usuário devera **informar, na URL** do WebSocket, a **sala** e o **nome do usuário**. 		

		ws://IP+porta/contexto/chat/{sala}/{usuario} 

Exemplo de uma requisição: 

		ws://localhost:8080/aplicacao/chat/política/bia

Solicitando entrar na sala chamada “politica”, com o nome de usuário “bia”. Observações:

 - Se a sala não existir, criá-la; 
 - Se o usuário já existe, crie um nome default e informe o novo nome ao usuário e a razão para isso. Ele poderá alterá-lo depois. 

### A Aplicação Cliente 

Desenvolva uma aplicação cliente que permita ao usuário participar de uma sala e enviar/receber mensagens. Nessa mesma aplicação, o usuário poderá visualizar, em tempo real, quem esta online na sala. E a qualquer momento, um usuário poderá deixar a sala. 
O usuário devera enviar as mensagens para o servidor, seguindo os formatos, para cada funcionalidade, de acordo com o quadro abaixo:

| Funcionalidade | Formato da Mensagem | Restrição
|--|--|--|
| Enviar mensagem geral |**send** \<mensagem\>| -
| Enviar mensagem reservada| **send –u** \<usuario\> \<mensagen\>| Notificar o usuário caso \<nome_usuario\> não exista.
| Renomear usuário | **rename** \<novo_nome\>| Notificar o usuário: a) Renomeado com sucesso ou b) Nome de usuário já em uso. |


**Observações**: 

Equipes com no **máximo DOIS** integrantes; 
Entrega e Defesa: 05/06/2018

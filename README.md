# gmequer
Aplicattivo para testar o MQSeries em ambientes de forma fácil 

# Como executar 
Baixar os arquivos 
- com.ibm.mq.allclient.jar
- gmequer.bat
- gmequer.jar
- gmequer.properties
- gmequer.sh
- javax.jms-api-2.0.1.jar
- json-20220320.jar

# Criar uma instância de MQ na máquina
Pode ser feito com o comando 
** docker run --name mqdemo --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --env MQ_ENABLE_METRICS=true --env MQ_ENABLE_EMBEDDED_WEB_SERVER=true --env MQ_ADMIN_PASSWORD=passw0rd --env MQ_APP_PASSWORD=passw0rd --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9009:9443 --publish 9157:9157 --detach ibmcom/mq **

# Alterar o arquivo gmequer.properties
1. hostName=127.0.0.1
2. hostPort=1414
3. channelName=DEV.APP.SVRCONN
4. queueManagerName=QM1
5. queueName=DEV.QUEUE.1
6. userName=app
7. userPassword=passw0rd
8. messageSizing=100
9. messagePattern=helloworld
10. numThreadsSender=50
11. numThreadsReceiver=50
12. sleepTime=1
13. persistent=false
14. statisticTime=5

# Opções :
hostName, hostPort, channelName, queueManager, queueName, userName e userPassword são as opções do MQ (o comando acima já cria com estas opções)
messageSizing - tamanho da mensagem - ela será criada concatenando suscessivamente o parametro messagePattern abaixo
messagePattern - usado para criar a mensagem
numThreadsSender - numero de threads para envio
numThreadsReceiver - numero de threads para recepção
sleepTime - tempo em milissegundos que o programa dorme antes de enviar ou ler outra mensagem
persistent - true se a mensagem é persistente ou false se não persistente
statisticTime - Tempo em segundos para apresentar as estatísticas 

# como executar
entrar no diretório em que baixou os arquivos e digitar 
./gmequer.sh - linux
gmequer.batt - Windows

# após executar serão impressos dados básicos 
GMEQUER 1.0
hostName 127.0.0.1
hostPort 1414
channelName DEV.APP.SVRCONN
queueManagerName QM1
queueName DEV.QUEUE.1
userName app
userPassword passw0rd
messageSizing 100
pattern helloworld
sleepTime 1
persistent false
numThreadsReceiver 50
numThreadsSender 50

# Aguarde um tempo até conectar com o servidor, e ecomeçarão a ser apresentadas mensagens como : 
--------------------------------
|PAC Enviados   |PAC Recebidos  |
|10038          |10034          |
|-------------------------------|
|TPS Send       |TPS Receive    |
|-------------------------------|
|2007           |2006           |
--------------------------------
|Errors   0     |
|-------------------------------|
São apresenttados os pacottes enviados e recebidos (numero), número de TPS (transações por segundo) no envio e na recepção e numero de erros

# comandos permitidos 
# exit - sai do programa (precisa aguardar as threads terminarem)
# error - descarrega na tela todos os erros acumulados até o momento
# stats - apresenta a configuração criada na conexão 

# Observações 
Você pode configurar o MQ apenas para enviar ou apenas para consumir, basta colocar 0 em numThreadsSender ou numThreadsReceiver


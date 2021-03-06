# gmequer
## Aplicativo para testar o MQSeries em ambientes de forma simplificada. 
## Ele permite esttressar e verificar limites de velocidade do IBM MQ
## Foi criado por chegar a um limite de throughput aparentemente intrasponível no JMETER


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
Pode ser feito com o comando </br>

<li>docker volume qm1data</li>
<li>docker run --name mqdemo --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --env MQ_ENABLE_METRICS=true --env MQ_ENABLE_EMBEDDED_WEB_SERVER=true --env MQ_ADMIN_PASSWORD=passw0rd --env MQ_APP_PASSWORD=passw0rd --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9009:9443 --publish 9157:9157 --detach ibmcom/mq</li>

## este utiliza a imagem aberta de demo do MQ no Docker.hub, e aponta para uma persistência externa (--volume)

## ou

<li>docker volume qm1data</li>
<li>docker run --name mqdemo --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --env MQ_ENABLE_METRICS=true --env MQ_ENABLE_EMBEDDED_WEB_SERVER=true --publish 1414:1414 --publish 9009:9443 --publish 9157:9157 --detach icr.io/ibm-messaging/mq</li>

## este utiliza a imagem IBM oficial, e cria a persistencia dentro do container (se deletar o container irá matar a fila)

## podem ser experimentadas variações dos dois comandos

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
1. hostName, hostPort, channelName, queueManager, queueName, userName e userPassword são as configurações do MQ 
2. (o comando acima já cria com estas opções, ou seja, se criar o MQ com o comando e rodar este arquivo sem alterar nada deve funcionar)
3. messageSizing - tamanho da mensagem - ela será criada concatenando suscessivamente o parametro messagePattern abaixo
4. messagePattern - usado para criar a mensagem
5. numThreadsSender - numero de threads para envio
6. numThreadsReceiver - numero de threads para recepção
7. sleepTime - tempo em milissegundos que o programa dorme antes de enviar ou ler outra mensagem
8. persistent - true se a mensagem é persistente ou false se não persistente
9. statisticTime - Tempo em segundos para apresentar as estatísticas 

# como executar
entrar no diretório em que baixou os arquivos e digitar </br>
./gmequer.sh - linux </br>
gmequer.bat - Windows </br>

# após executar serão impressos dados básicos 
<li> GMEQUER 1.0 </li>
<li> hostName 127.0.0.1</li>
<li> hostPort 1414</li>
<li> channelName DEV.APP.SVRCONN</li>
<li> queueManagerName QM1</li>
<li> queueName DEV.QUEUE.1</li>
<li> userName app</li>
<li> userPassword passw0rd</li>
<li> messageSizing 100</li>
<li> pattern helloworld</li>
<li> sleepTime 1</li>
<li> persistent false</li>
<li> numThreadsReceiver 50</li>
<li> numThreadsSender 50</li>

# Aguarde um tempo até conectar com o servidor, e ecomeçarão a ser apresentadas mensagens como : 

--------------------------------</br>
|PAC Enviados   |PAC Recebidos  |</br>
|10038          |10034          |</br>
|-------------------------------|</br>
|TPS Send       |TPS Receive    |</br>
|-------------------------------|</br>
|2007           |2006           |</br>
--------------------------------</br>
|Errors   0     |</br>
|-------------------------------|</br>

### São apresentados os pacotes enviados e recebidos (numero), número de TPS (transações por segundo) no envio e na recepção e numero de erros
![](tela.png)

# comandos permitidos 
<li>exit - sai do programa (precisa aguardar as threads terminarem)</li>
<li>error - descarrega na tela todos os erros acumulados até o momento</li>
<li>stats - apresenta a configuração criada na conexão </li>

# Observações 
<li>Você pode configurar o MQ apenas para enviar ou apenas para consumir, basta colocar 0 em numThreadsSender ou numThreadsReceiver</li>
<li>Apos digitar a o docker run acima. um console do MQ será aberto na url </li>
!(https://localhost:9009/)

![](console.png)

# usuarios
## admin - passw0rd  (administração e console)
## app - passw0rd (conexão do programa)


# Códigos
## para compilar, crie um projeto no seu IDE ou maven, e aponte os JARs acima no classpath do compilador (menos o gmequer.jar)

# Diagrama de classes 
![](classes.png)

# nanoIB 2.0

nanoIB é uma aplicação web fictícia, que simula o funcionamento básico de aplicações de Internet/Mobile Banking. Destina-se a assistir o desenvolvimento de técnicas de detecção e mitigação de backdoors em aplicações web, servindo como um objeto de análise livre das complexidades operacionais de uma aplicação real.

A arquitetura do nanoIB é formada por três elementos:

* um conjunto de páginas HTML dinâmicas;
* uma aplicação servidora que executa um web service RESTful que é consultado pelas páginas dinâmicas;
* um conjunto de stored procedures executadas em DBMS, simulando o funcionamento de transações classicamente executadas em mainframes de ambientes computacionais bancários.

Para testar a influência de estruturas de frameworks atuais na performance de SATs a aplicação foi desenvolvida em Spring Boot.  

## Estrutura do repositório

O repositório do projeto tem a seguinte estrutura:

* /domain: camada de dominio, define as entidades, suas lógicas de negocio e a interface dos repositorios.

* /infra: camada de infraestrutura, implementa as interfaces e serviços da camada de domain:
  * /Jdbc: imlementação dos repositorios da camada de dominio.
  * /http: contem os controllers e lógica de rotas como tratamento de erros de permissões (Spring Boot Security).

* /resources: arquivos estáticos e script de criação da base de dados.

## Requisitos

* Apache2
* MariaDB 10.3
* Apache 2.4.51
* JDK 1.8
* Spring Boot 2.5.3

## Instalação e Execução no Windows
Seja *<user\>* mysql root user, e *<parent-folder\>* o a pasta que contem o nanoib:
```bash
mysql -u <user> -p<  <parent-folder>\nanoibspringboot\resources\db.sql
```

O Apache irá servir os arquivos estáticos e funcionar como proxy reverso para a API REST. 
Assim, no arquivo *<parent-folder\>\Apache24\conf\httpd.conf* descomentar as seguinter linhas *(geralmente Apache24 se encontra no diretorio C:\ do sistema)*:
```bash
LoadModule proxy_module modules/mod_proxy.so
LoadModule proxy_http_module modules/mod_proxy_http.so
```
Depois, adicionar a configuração de proxy para a porta 8080 (Web Server do Tomcat):
```bash
<VirtualHost *:80>
    ProxyPreserveHost On
    ProxyPass        /api/v1/ http://localhost:8080/api/v1/
    ProxyPassReverse /api/v1/ http://localhost:8080/api/v1/
</VirtualHost>
```
Finalmente, para servir o conteudo estático, criar uma pasta "www" nas configurações do apache e mover todos os htmls para ela:
```bash
cd <repo-parent>\Apache24
mkdir www
move <repo-parent>\nanoibspringboot\resources\*.html .\www
```
E modificar o httpd.conf para procurar nessa pasta e proporcionar todos os permisos:
```bash
DocumentRoot "${SRVROOT}/www"
<Directory "${SRVROOT}/www">
    Options Indexes FollowSymLinks
    AllowOverride All
    Require all granted
</Directory>
```
Para executar o projeto, apenas clickar "Run" no IDE do projeto para rodar o Web Server e executar o arquivo *http.exe* do apache:
```bash
.\<parent-folder/>\Apache24\bin\httpd.exe
```
Desse modo, o server Apache (na porta 80) serve os arquivos html e a API REST (na porta 8080) atende as requisições.
Com os dois processos rodando, abrir o navegador em "localhost:80".

## Instalação e Execução no Linux

Em Linux:

```bash
sudo mysql < <repo-folder>/resources/db.sql
```
Em seguida, deve-se habilitar o modo reverse-proxy do Apache2:

```bash
sudo a2enmod proxy
sudo a2enmod proxy_http
```

E então deve-se editar o arquivo de configuração do Apache2:

```bash
sudo nano /etc/apache2/sites-available/000-default.conf
```

Adicionando-se o seguinte conteúdo ao elemento "VirtualHost":
 
```xml
<VirtualHost *:80>
    ProxyPreserveHost On
    ProxyPass        /api/v1/ http://localhost:8080/api/v1/
    ProxyPassReverse /api/v1/ http://localhost:8080/api/v1/
</VirtualHost>
```

Então, deve-se reiniciar o serviço do Apache:

```bash
sudo systemctl restart apache2
```

Em seguida, deve-se copiar os arquivos HTML para o diretório "www" do Apache, e ajustar-se a permissão de leitura dos mesmos:

```bash
sudo mkdir /var/www/html/nanoib
sudo cp * /var/www/html/nanoib
cd /var/www/html/nanoib
sudo chmod 644 *
```

Por fim, deve-se importar o código da aplicação servidora no Eclipse como um projeto Maven. Então, deve-se clicar com o botão direito sobre o projeto "nanoib-project", escolher a opção "Run as -> Maven build", com goal "compile". Depois de compilada, deve-se rodar a aplicação a partir da classe "Main", e acessá-la através de "http://localhost/nanoib". Pode ser necessário ajustar configurações de firewall, caso se queira acessar a aplicação através da rede. Credenciais de teste podem ser lidos do script de criação da base de dados.
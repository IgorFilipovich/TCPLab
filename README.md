# TCPLab
## Реализация
### 1. Alice
Создаем Socket для заданного порта и адреса  
    1. Читаем stdin, отправляем на сокет.  
    2. Читаем из сокета, пишем на stdout.
### 2. Bob
Создаем ServerSocket и слушаем заданный порт, когда кто-то до нас достучался, 
получаем его сокет. Дальше все аналогично: слушаем, пересылаем.
### 3. Mike
Совмещаем Боба и Алису, слушаем на одном порту, пересылаем в другой между этим
просматривая сообщение.
### 4. Security
Первым сообщением обмениваемся ключами с помощью Diffie-Hellman-а, получаем ключ
для симметричного шифрования. Дальше каждое сообщение шифруем с помощью AES. 
Вот и все шифрование, к счастью на ~~Java~~ Kotlin, то все уже реализовано,
главное правильно все совместить.  
#### Replay attack
В данной версии возможна. 

 Как исправить: с помощью Diffie-Hellman-а генерируем ключ длины 512 бит, для AES надо от 128 до 256.  
Поэтому можем для каждого сообщения использовать различный сдвиг в ключе из Diffie-Hellman-а,
чтобы получать ключ для симметричного шифрования.  
Вопрос как синхронизировать сдвиг. Есть 2 варианта, обмениваться сразу какой-то 
последовательностью ключей, либо передавать сдвиг для следующего сообщения в предыдущем.
#### Может ли Майк расшифровать предыдущие сессии, если получит доступ к ключу Боба от одной из последующих
Конечно, может.  
Чтобы избежать этого, можем генерировать ключ для симметричного шифрования
и обмениваться им с помощью Diffie-Hellman-a для каждых k сессий. В лучшем
случае k = 1, но это возможно долго, поэтому выбор k оставим читателю.
### 5. Mike gets disclosed.
Не судьба, к сожалению.
## Компиляция
TBD
## Запуск
Запускаем как обычный бинарь

    ./prog_name arguments

Аргументы передаются такие же, как и в условии.  
Чтобы запустить Alice и Bob с шифрованием, каждой программе последним аргументом надо
передать "true"

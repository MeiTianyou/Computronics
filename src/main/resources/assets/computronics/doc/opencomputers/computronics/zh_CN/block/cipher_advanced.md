# 高级加密机

![信息安全了，暂时。](block:computronics:cipher_advanced)

高级加密机使用了RSA加密法，它会创建一对公钥与私钥。密钥对可使用一对质数生成，也可随机生成。生成完毕后，返回值中将会同时包含公钥和私钥。（生成过程会耗费数秒）
公钥用于加密信息，而私钥用于解密信息。只有拥有私钥的电脑才能解密信息。
package app.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Класс хеширует пароль в соответствии со схемой OpenBSD bcrypt.
 */
public class PswdCrypt {

    // Экспоненциальная стоимость (коэффициэнт log2) между BCrypt.MIN_COST и 
    // BCrypt.MAX_COST Например, 12 -> 2 ^ 12 = 4096 итераций
    private final int cost = 12;

    /**
     * Хеширует пароль в соответствии со схемой OpenBSD bcrypt.
     *
     * @param password пароль пользователя
     * @return строка в кодировке utf-8, которая включает версию, коэффициент
     * стоимости, соль и необработанный хеш (как radix64)
     */
    public String pswdCrypt(String password) {
        return BCrypt.withDefaults().hashToString(cost, password.toCharArray());
    }

    /**
     * Метод сравнивает заданный хэш с заданным паролем
     *
     * @param password пароль пользователя
     * @param bcryptHashString хэш для сравнения
     * @return true - если пароль и хэш совпадают, false - если не совпадают
     */
    public boolean pswdVerified(String password, String bcryptHashString) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bcryptHashString);
        return result.verified;
    }
}

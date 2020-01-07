package me.egg82.ae.enums;

import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

public enum Message implements MessageKeyProvider {
    GENERAL__HEADER,
    GENERAL__ENABLED,
    GENERAL__DISABLED,
    GENERAL__LOAD,
    GENERAL__HOOK_ENABLE,
    GENERAL__HOOK_DISABLE,
    GENERAL__UPDATE,

    ERROR__INTERNAL,
    ERROR__NO_CONSOLE,
    ERROR__ENCHANT_NOT_FOUND,
    ERROR__NO_ITEM,

    PLAYER__SOUL_VANISHED,
    PLAYER__SILENCED,

    RELOAD__BEGIN,
    RELOAD__END,

    SET__ERROR_LEVEL_MIN,
    SET__ERROR_LEVEL_MAX,
    SET__ERROR_CONFLICTS,
    SET__SUCCESS_MAIN_HAND,
    SET__SUCCESS_OFF_HAND,

    REMOVE__SUCCESS_MAIN_HAND,
    REMOVE__SUCCESS_OFF_HAND,

    SOULS__ERROR_MIN,
    SOULS__SUCCESS_MAIN_HAND,
    SOULS__SUCCESS_OFF_HAND;

    private final MessageKey key = MessageKey.of(name().toLowerCase().replace("__", "."));
    public MessageKey getMessageKey() { return key; }
}

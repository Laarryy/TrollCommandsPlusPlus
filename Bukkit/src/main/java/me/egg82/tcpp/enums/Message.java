package me.egg82.tcpp.enums;

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

    ALONE__START,
    ALONE__STOP,
    AMNESIA__START,
    AMNESIA__STOP,
    ANNOY__START,
    ANNOY__STOP,
    ANVIL__START,
    ATTACH__NEED_ITEM,
    ATTACH__START,
    ATTACH__STOP,
    BANISH__NO_LOCATIONS,
    BANISH__START,
    CONTROL__START,
    CONTROL__STOP,
    FREEZE__START,
    FREEZE__STOP,
    GARBLE__START,
    GARBLE__STOP,
    LIFT__START,
    LIFT__STOP,
    SNOWBALLFIGHT__START,
    SNOWBALLFIGHT__STOP,

    ERROR__INTERNAL,
    ERROR__NO_CONSOLE,
    ERROR__PLAYER_OFFLINE,
    ERROR__PLAYER_NOT_FOUND,
    ERROR__TROLL_NOT_FOUND,
    ERROR__PLAYER_IMMUNE,
    ERROR__COMMAND_NO_PROTOCOLLIB,

    RELOAD__BEGIN,
    RELOAD__END;

    private final MessageKey key = MessageKey.of(name().toLowerCase().replace("__", "."));
    public MessageKey getMessageKey() { return key; }
}

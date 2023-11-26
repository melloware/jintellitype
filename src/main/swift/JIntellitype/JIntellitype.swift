import Foundation
import HotKey
import AppKit

var hotkeys: [jint: HotKey] = [:]
var jvm: UnsafeMutablePointer<JavaVM?>? = nil
var staticJobj: jobject? = nil
var methodID: jmethodID? = nil

@_silgen_name("Java_com_melloware_jintellitype_JIntellitype_initializeLibrary")
public func initializeLibrary(env: UnsafeMutablePointer<JNIEnv?>, jobj: jobject) {
    let jni = env.pointee!.pointee
    let jvmPointer = UnsafeMutablePointer<UnsafeMutablePointer<JavaVM?>?>.allocate(capacity: 1)
    let _ = jni.GetJavaVM(env, jvmPointer)
    jvm = jvmPointer.pointee
    staticJobj = jni.NewGlobalRef(env, jobj)
    
    let methodName = "onHotKey"
    let methodSignature = "(I)V"
    let javaClass = jni.GetObjectClass(env, jobj)
    methodID = jni.GetMethodID(env, javaClass, methodName, methodSignature)
}

@_silgen_name("Java_com_melloware_jintellitype_JIntellitype_terminate")
public func terminate(env: UnsafeMutablePointer<JNIEnv?>, jobj: jobject) {
    hotkeys.removeAll()
    let _ = jvm!.pointee!.pointee.DetachCurrentThread(jvm)
}

@_silgen_name("Java_com_melloware_jintellitype_JIntellitype_regHotKey")
public func regHotKey(env: UnsafeMutablePointer<JNIEnv?>, jobj: jobject, identifier: jint, modifier: jint, keycode: jint) {
    let hotKey = HotKey(key: getKeyFromJint(keyAsJint: keycode), modifiers: getModifiersFromJint(modifiersAsJint: modifier))
    hotkeys[identifier] = hotKey
    hotKey.keyDownHandler = {
        let envpointerRaw = UnsafeMutablePointer<UnsafeMutableRawPointer?>.allocate(capacity: 1)
        let _ = jvm!.pointee!.pointee.AttachCurrentThread(jvm, envpointerRaw, nil)
        let envpointer = envpointerRaw.pointee!.assumingMemoryBound(to: JNIEnv?.self)
        let jni = envpointer.pointee!.pointee

        let valueAsJValue = jvalue(i: identifier)
        var methodArgs: [jvalue] = [valueAsJValue]
        jni.CallVoidMethodA(envpointer, staticJobj!, methodID!, &methodArgs)
        
    }
}
@_silgen_name("Java_com_melloware_jintellitype_JIntellitype_unregHotKey")
public func unregHotKey(env: UnsafeMutablePointer<JNIEnv?>, jobj: jobject, identifier: jint) {
    hotkeys.removeValue(forKey: identifier)
}
@_silgen_name("Java_com_melloware_jintellitype_JIntellitype_isRunning")
public func isRunning(env: UnsafeMutablePointer<JNIEnv?>, jobj: jobject) -> jboolean {
    // On Windows this prevents starting up the hidden window twice.
    // As this is no restriction on Mac, we always return false.
    return 0
}

private func getModifiersFromJint(modifiersAsJint: jint) -> NSEvent.ModifierFlags {
    var modifiers: NSEvent.ModifierFlags = []
    if (modifiersAsJint & 1 == 1) { // MOD_ALT_OR_OPTION
        modifiers.insert(.option)
    }
    if (modifiersAsJint & 2 == 2) { // MOD_CONTROL
        modifiers.insert(.control)
    }
    if (modifiersAsJint & 4 == 4) { // MOD_SHIFT
        modifiers.insert(.shift)
    }
    if (modifiersAsJint & 8 == 8) { // MOD_META
        modifiers.insert(.command)
    }
    return modifiers
}

private func getKeyFromJint(keyAsJint: jint) -> Key {
    switch keyAsJint {
    case 9: return .tab
    case 13: return .return
    case 27: return .escape
    case 32: return .space
    case 33: return .pageUp
    case 34: return .pageDown
    case 35: return .end
    case 36: return .home
    case 37: return .leftArrow
    case 38: return .upArrow
    case 39: return .rightArrow
    case 40: return .downArrow
    case 46: return .delete
    case 48: return .zero
    case 49: return .one
    case 50: return .two
    case 51: return .three
    case 52: return .four
    case 53: return .five
    case 54: return .six
    case 55: return .seven
    case 56: return .eight
    case 57: return .nine
    case 65: return .a
    case 66: return .b
    case 67: return .c
    case 68: return .d
    case 69: return .e
    case 70: return .f
    case 71: return .g
    case 72: return .h
    case 73: return .i
    case 74: return .j
    case 75: return .k
    case 76: return .l
    case 77: return .m
    case 78: return .n
    case 79: return .o
    case 80: return .p
    case 81: return .q
    case 82: return .r
    case 83: return .s
    case 84: return .t
    case 85: return .u
    case 86: return .v
    case 87: return .w
    case 88: return .x
    case 89: return .y
    case 90: return .z
    case 96: return .keypad0
    case 97: return .keypad1
    case 98: return .keypad2
    case 99: return .keypad3
    case 100: return .keypad4
    case 101: return .keypad5
    case 102: return .keypad6
    case 103: return .keypad7
    case 104: return .keypad8
    case 105: return .keypad9
    case 109: return .minus
    case 110: return .period
    case 112: return .f1
    case 113: return .f2
    case 114: return .f3
    case 115: return .f4
    case 116: return .f5
    case 117: return .f6
    case 118: return .f7
    case 119: return .f8
    case 120: return .f9
    case 121: return .f10
    case 122: return .f11
    case 123: return .f12
    case 186: return .semicolon
    case 187: return .equal
    case 188: return .comma
    case 191: return .slash
    case 219: return .leftBracket
    case 220: return .backslash
    case 221: return .rightBracket
    case 222: return .quote
        
    default:
        fatalError("The given key is not supported")
    }
}

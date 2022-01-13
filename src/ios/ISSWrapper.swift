import Foundation

@objc(ISSWrapper)
public class ISSWrapper : CDVPlugin {
    @objc(echo:)
  func echo(_ command: CDVInvokedUrlCommand) {
    let echo = command.argument(at: 0) as! String?
    let pluginResult:CDVPluginResult

    if echo != nil && echo!.count > 0 {
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: echo!)
    } else {
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_ERROR)
    }

    self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
  }
    
    @objc(isReverseEngineeringDetected:)
    func isReverseEngineeringDetected(command: CDVInvokedUrlCommand) {
        let isReversedEngineered = IOSSecuritySuite.amIReverseEngineered()
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isReversedEngineered)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(isJailbrokenDetected:)
    func isJailbrokenDetected(command: CDVInvokedUrlCommand) {
        let isJailbroken = IOSSecuritySuite.amIJailbroken()
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isJailbroken)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(isDebuggerDetected:)
    func isDebuggerDetected(command: CDVInvokedUrlCommand) {
        let isDebugged = IOSSecuritySuite.amIDebugged()
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isDebugged)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(isEmulationDetected:)
    func isEmulationDetected(command: CDVInvokedUrlCommand) {
        let isEmulated = IOSSecuritySuite.amIRunInEmulator()
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isEmulated)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(isProxyDetected:)
    func isProxyDetected(command: CDVInvokedUrlCommand) {
        let isProxied = IOSSecuritySuite.amIProxied()
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isProxied)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
    
    @objc(isTampered:)
    func isTampered(command: CDVInvokedUrlCommand) {
        var isTampered:Bool
        
        if IOSSecuritySuite.amITampered([.bundleID("biz.securing.FrameworkClientApp"),
            .mobileProvision("2976c70b56e9ae1e2c8e8b231bf6b0cff12bbbd0a593f21846d9a004dd181be3"),
            .machO("IOSSecuritySuite", "6d8d460b9a4ee6c0f378e30f137cebaf2ce12bf31a2eef3729c36889158aa7fc")]).result {
            isTampered = true
        }
        else {
            isTampered = false
        }
        
        let pluginResult:CDVPluginResult
        
        pluginResult = CDVPluginResult.init(status: CDVCommandStatus_OK, messageAs: isTampered)
        
        self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    }
}

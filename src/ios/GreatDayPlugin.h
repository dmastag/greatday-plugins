#import <Cordova/CDV.h>

@interface GreatDayPlugin : CDVPlugin

// Public static method
+ (GreatDayPlugin*) greatDayPlugin;

// A public instance method
- (void)logMessage: (NSString*)msg;
- (NSString *) getMessage;
- (void)addBlurredSnapshot:(CDVInvokedUrlCommand*)command;
- (void)removeBlurredSnapshot:(CDVInvokedUrlCommand*)command;
- (void)setUserAgent:(CDVInvokedUrlCommand*)command;

@end

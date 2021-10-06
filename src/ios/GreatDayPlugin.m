#import "GreatDayPlugin.h"
#import "AppDelegate.h"

@implementation GreatDayPlugin

// Private static reference
static GreatDayPlugin* greatDayPlugin;

NSString *message;

// Public static method
+ (GreatDayPlugin*) greatDayPlugin {
    return greatDayPlugin;
}


// implement CDVPlugin delegate
- (void)pluginInitialize {
    greatDayPlugin = self;
    message = @"BlurredSnapshot OFF";
}


// A public instance method
- (void)logMessage: (NSString*)msg
{
    NSLog(@"MyPlugin: 0 %@", msg);
    message = msg;
}

- (void)addBlurredSnapshot:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;
    
    [GreatDayPlugin.greatDayPlugin logMessage:@"BlurredSnapshot ON"];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)removeBlurredSnapshot:(CDVInvokedUrlCommand*)command {
    CDVPluginResult* pluginResult = nil;
    
    [GreatDayPlugin.greatDayPlugin logMessage:@"BlurredSnapshot OFF"];
    
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setUserAgent: (CDVInvokedUrlCommand*)command
{
    id newUserAgent = [command argumentAtIndex:0];
    
    NSDictionary *dictionary = [NSDictionary dictionaryWithObjectsAndKeys:newUserAgent, @"UserAgent", nil];
     [[NSUserDefaults standardUserDefaults] registerDefaults:dictionary];
    
    NSString* callbackId = command.callbackId;
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:newUserAgent];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}


// A public instance method
- (NSString *) getMessage{
    
    NSLog(@"MyPlugin: 1 %@", message);

    if ( message == nil ) {
        message = @"BlurredSnapshot OFF";
    }
    return message;
}

@end

@interface AppDelegate (GreatDayPlugin) {}

@end

@implementation AppDelegate (GreatDayPlugin)

- (void)applicationWillResignActive:(UIApplication *)application {
    
    NSString *msg = [GreatDayPlugin.greatDayPlugin getMessage];
    NSLog(@"MyPlugin: 2 %@", msg);
    
    if([msg isEqualToString:@"BlurredSnapshot ON"]) {
        self.window.backgroundColor = [UIColor clearColor];

        UIBlurEffect *blurEffect = [UIBlurEffect effectWithStyle:UIBlurEffectStyleExtraLight];
        UIVisualEffectView *blurEffectView = [[UIVisualEffectView alloc] initWithEffect:blurEffect];
        blurEffectView.frame = self.window.bounds;
        blurEffectView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

        blurEffectView.tag = 1234;
        blurEffectView.alpha = 0;
        [self.window addSubview:blurEffectView];
        [self.window bringSubviewToFront:blurEffectView];

        // fade in the view
        [UIView animateWithDuration:0.5 animations:^{
            blurEffectView.alpha = 1;
        }];
    }
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    NSLog(@"Hiding blur");
    // grab a reference to our custom blur view
    UIView *blurEffectView = [self.window viewWithTag:1234];

    // fade away colour view from main view
    [UIView animateWithDuration:0.5 animations:^{
        blurEffectView.alpha = 0;
    } completion:^(BOOL finished) {
        // remove when finished fading
        [blurEffectView removeFromSuperview];
    }];
}

@end

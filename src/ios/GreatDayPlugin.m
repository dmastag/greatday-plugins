#import "AppDelegate.h"

@interface AppDelegate (GreatDayPlugin) {}

- (void)addBlurredSnapshot:(CDVInvokedUrlCommand*)command;
- (void)removeBlurredSnapshot:(CDVInvokedUrlCommand*)command;

@end

char status = 'o';

@implementation AppDelegate (GreatDayPlugin)

- (void)addBlurredSnapshot:(CDVInvokedUrlCommand*)command {
    status = 'a';
}

- (void)removeBlurredSnapshot:(CDVInvokedUrlCommand*)command {
    status = 'o';
}


- (void)applicationWillResignActive:(UIApplication *)application {
    if(status != 'o') {
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

//
//  MyPlugins.h
//  Unity-iPhone
//
//  Created by 丧尸喵 on 2020/4/4.
//

#import <vector>
#import <Foundation/Foundation.h>
#import <IJKMediaFramework/IJKMediaFramework.h>

@interface MyPlugins : NSObject
@property(nonatomic,strong) id<IJKMediaPlayback> player;
@end

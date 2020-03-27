/**
 * Copyright 2019 Tim Lindquist,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: Two views and viewcontrollers that show interaction between foreground and
 * background activities and views in an iOS app.
 * Use this example as a basis for developing an app which demonstrates all of the
 * life-cycle methods. Explain the state changes that cause each to
 * life-cycle method be executed.
 *
 * This app includes a seque transitioning from one view contoller (ViewController) to
 * another (SecondViewController) via a push seque. This transition is constructed in
 * the storyboard. Returning (popping) from the second is done in a button handler.
 * It is the IBAction popClicked
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version April, 2019
 */

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        NSLog("In ViewController, viewDidLoad")
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
        NSLog("In ViewController, didReceiveMemoryWarning")
    }

    // this example overrides two of the four other view conroller life-cycle methods.
    // here: viewWillAppear and viewDidDisappear
    // examine how/when each is called by overrideing them all and observing when each
    // is called. Do this in both view controllers.
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NSLog("In ViewController, viewWillAppear")
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NSLog("In ViewController, viewDidDisappear")
    }
}


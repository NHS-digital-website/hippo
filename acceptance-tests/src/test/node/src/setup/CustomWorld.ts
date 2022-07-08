import { World, setWorldConstructor, IWorldOptions } from '@cucumber/cucumber';
import BrowserDelegate, {Viewport} from "./BrowserDelegate";
import matchSnapshot from './snapshot-matcher';

export class CustomWorld extends World {
    currentPage?: string;
    readonly browser: BrowserDelegate;

    constructor(options: IWorldOptions) {
        super(options);
        this.browser = new BrowserDelegate();
    }

    matchSnapshot = matchSnapshot.bind(this);
}

setWorldConstructor(CustomWorld);

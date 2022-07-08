/*
 * Playwright's .toHaveScreenshot() must be ran within the context of a Playwright Test Runner test
 * Using 'pixelmatch' as an alternative
 */

import * as path from "path";
import { promises as fs } from "fs";
import {PNG} from "pngjs";
import pixelmatch = require("pixelmatch");

export default async function matchSnapshot(imageBuff: Buffer, referenceFile: string) {
    const refFileName = `${referenceFile}.png`;
    const refFileDir = path.resolve(__dirname, '../../../snapshots')
    const refFilePath = `${refFileDir}/${refFileName}`;

    let referenceBuff = null;
    try {
        referenceBuff = await fs.readFile(refFilePath);
    } catch (e) {
        console.info('Screenshot not found');
    }

    if (referenceBuff == null || process.env.UPDATE_SNAPSHOTS) {
        await fs.writeFile(refFilePath, imageBuff);
        console.info(`Added reference image, "${refFileName}"`);
        return true;
    }

    try {
        const referenceBuff = await fs.readFile(refFilePath);
        const refImg = PNG.sync.read(referenceBuff);
        const compImg = PNG.sync.read(imageBuff);
        const diffImg = new PNG({width: refImg.width, height: refImg.height});

        const result = pixelmatch(compImg.data, refImg.data, diffImg.data, refImg.width, refImg.height,{threshold: 0.15});
        if (result > 0) {
            const diffBuffer = PNG.sync.write(diffImg);
            this.attach(diffBuffer, 'image/png');
            return false;
        }
        return true;
    } catch(e) {
        this.attach((e as Error).message);
        return false;
    }
}

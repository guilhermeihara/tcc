/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { Contract } = require('fabric-contract-api');

async function savePrivateData(ctx, assetKey) {
    const mspid = ctx.clientIdentity.getMSPID();
    const collection = '_implicit_org_' + mspid;

    const transientMap = ctx.stub.getTransient();
    if (transientMap) {
        const properties = transientMap.get('asset_properties');
        if (properties) {
            await ctx.stub.putPrivateData(collection, assetKey, properties);
        }
    }
}

async function removePrivateData(ctx, assetKey) {
    const mspid = ctx.clientIdentity.getMSPID();
    const collection = '_implicit_org_' + mspid;

    const propertiesBuffer = await ctx.stub.getPrivateData(collection, assetKey);
    if (propertiesBuffer && propertiesBuffer.length > 0) {
        await ctx.stub.deletePrivateData(collection, assetKey);
    }
}

async function readState(ctx, id) {
    const assetBuffer = await ctx.stub.getState(id); // get the asset from chaincode state
    if (!assetBuffer || assetBuffer.length === 0) {
        throw new Error(`The asset ${id} does not exist`);
    }
    const assetString = assetBuffer.toString();
    const asset = JSON.parse(assetString);

    return asset;
}


class TccContract extends Contract {
    async CreateAsset(ctx, address, owner, name, value) {
        const asset = {
            name: name,
            value: value,
            owner: owner,
            address: address,
        };
        await savePrivateData(ctx, address);
        const assetBuffer = Buffer.from(JSON.stringify(asset));

        ctx.stub.setEvent('CreateAsset', assetBuffer);
        return ctx.stub.putState(address, assetBuffer);
    }

    // ReadAsset returns the asset stored in the world state with given id.
    async ReadAsset(ctx, address) {
        const assetJSON = await ctx.stub.getState(address); // get the asset from chaincode state
        if (!assetJSON || assetJSON.length === 0) {
            throw new Error(`The asset ${address} does not exist`);
        }
        return assetJSON.toString();
    }

    // UpdateAsset updates an existing asset in the world state with provided parameters.
    async UpdateAsset(ctx, address, name, value) {
        const asset = await readState(ctx, address);

        const updateEvent = {
            name: name,
            address: address,
            oldValue: asset.value,
            newValue: value,
        };

        asset.name = name;
        asset.value = value;
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        const eventBuffer = Buffer.from(JSON.stringify(updateEvent));

        await savePrivateData(ctx, address);

        ctx.stub.setEvent('UpdateAsset', eventBuffer);
        return ctx.stub.putState(address, assetBuffer);
    }

    // DeleteAsset deletes an given asset from the world state.
    async DeleteAsset(ctx, address) {
        const asset = await readState(ctx, address);
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        await removePrivateData(ctx, address);

        ctx.stub.setEvent('DeleteAsset', assetBuffer);
        return ctx.stub.deleteState(address);
    }

    // AssetExists returns true when asset with given ID exists in world state.
    async AssetExists(ctx, address) {
        const assetJSON = await ctx.stub.getState(address);
        return assetJSON && assetJSON.length > 0;
    }

    // GetAllAssets returns all assets found in the world state.
    async GetAllAssets(ctx) {
        const allResults = [];
        // range query with empty string for startKey and endKey does an open-ended query of all assets in the chaincode namespace.
        const iterator = await ctx.stub.getStateByRange('', '');
        let result = await iterator.next();
        while (!result.done) {
            const strValue = Buffer.from(result.value.value.toString()).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: result.value.key, Record: record });
            result = await iterator.next();
        }
        return JSON.stringify(allResults);
    }

}

module.exports = TccContract;

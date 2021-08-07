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
    async CreateAsset(ctx, id, address, name, value) {
        const asset = {
            id: id,
            name: name,
            value: value,
            address: address
        };
        await savePrivateData(ctx, id);
        const assetBuffer = Buffer.from(JSON.stringify(asset));

        ctx.stub.setEvent('CreateAsset', assetBuffer);
        return ctx.stub.putState(id, assetBuffer);

        // ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
        // return JSON.stringify(asset);
    }

    // ReadAsset returns the asset stored in the world state with given id.
    async ReadAsset(ctx, id) {
        const assetJSON = await ctx.stub.getState(id); // get the asset from chaincode state
        if (!assetJSON || assetJSON.length === 0) {
            throw new Error(`The asset ${id} does not exist`);
        }
        return assetJSON.toString();
    }

    // UpdateAsset updates an existing asset in the world state with provided parameters.
    async UpdateAsset(ctx, id, name, value) {
        const asset = await readState(ctx, id);

        const updateEvent = {
            id: id,
            name: asset.Name,
            address: asset.Address,
            oldValue: asset.Value,
            newValue: value,
        };

        asset.Name = name;
        asset.Value = value;
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        const eventBuffer = Buffer.from(JSON.stringify(updateEvent));

        await savePrivateData(ctx, id);

        ctx.stub.setEvent('UpdateAsset', eventBuffer);
        return ctx.stub.putState(id, assetBuffer);

        // const exists = await this.AssetExists(ctx, id);
        // if (!exists) {
        //     throw new Error(`The asset ${id} does not exist`);
        // }

        // // overwriting original asset with new asset
        // const updatedAsset = {
        //     ID: id,
        //     Name: name,
        //     Value: value,
        // };
        // return ctx.stub.putState(id, Buffer.from(JSON.stringify(updatedAsset)));
    }

    // DeleteAsset deletes an given asset from the world state.
    async DeleteAsset(ctx, id) {
        const asset = await readState(ctx, id);
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        await removePrivateData(ctx, id);

        ctx.stub.setEvent('DeleteAsset', assetBuffer);
        return ctx.stub.deleteState(id);
    }

    // AssetExists returns true when asset with given ID exists in world state.
    async AssetExists(ctx, id) {
        const assetJSON = await ctx.stub.getState(id);
        return assetJSON && assetJSON.length > 0;
    }

    // TransferAsset updates the owner field of asset with given id in the world state.
    // async TransferAsset(ctx, id, newOwner) {
    //     const assetString = await this.ReadAsset(ctx, id);
    //     const asset = JSON.parse(assetString);
    //     asset.Owner = newOwner;
    //     return ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
    // }

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

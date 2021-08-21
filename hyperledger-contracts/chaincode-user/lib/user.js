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


class UserContract extends Contract {
    async CreateUser(ctx, email, password, name) {
        const asset = {
            name: name,
            email: email,
            password: password
        };
        await savePrivateData(ctx, email);
        const assetBuffer = Buffer.from(JSON.stringify(asset));

        ctx.stub.setEvent('CreateUser', assetBuffer);
        return ctx.stub.putState(email, assetBuffer);
    }

    // ReadAsset returns the asset stored in the world state with given id.
    async ReadUser(ctx, email) {
        const assetJSON = await ctx.stub.getState(email); // get the asset from chaincode state
        if (!assetJSON || assetJSON.length === 0) {
            throw new Error(`The user ${email} does not exist`);
        }
        return assetJSON.toString();
    }

    // UpdateAsset updates an existing asset in the world state with provided parameters.
    async UpdateUser(ctx, email, password, name) {
        const asset = await readState(ctx, email);

        const updateEvent = {
            email: email,
            name: name,
            oldPassword: asset.password,
            newPassword: password,
        };

        asset.name = name;
        asset.password = password;
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        const eventBuffer = Buffer.from(JSON.stringify(updateEvent));

        await savePrivateData(ctx, email);

        ctx.stub.setEvent('UpdateUser', eventBuffer);
        return ctx.stub.putState(email, assetBuffer);
    }

    // DeleteAsset deletes an given asset from the world state.
    async DeleteUser(ctx, email) {
        const asset = await readState(ctx, email);
        const assetBuffer = Buffer.from(JSON.stringify(asset));
        await removePrivateData(ctx, email);

        ctx.stub.setEvent('DeleteUser', assetBuffer);
        return ctx.stub.deleteState(email);
    }

    // AssetExists returns true when asset with given ID exists in world state.
    async UserExists(ctx, email) {
        const assetJSON = await ctx.stub.getState(email);
        return assetJSON && assetJSON.length > 0;
    }

    async GetAllUsers(ctx) {
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

module.exports = UserContract;

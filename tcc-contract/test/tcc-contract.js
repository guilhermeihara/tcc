/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { ChaincodeStub, ClientIdentity } = require('fabric-shim');
const { TccContract } = require('..');
const winston = require('winston');

const chai = require('chai');
const chaiAsPromised = require('chai-as-promised');
const sinon = require('sinon');
const sinonChai = require('sinon-chai');

chai.should();
chai.use(chaiAsPromised);
chai.use(sinonChai);

class TestContext {

    constructor() {
        this.stub = sinon.createStubInstance(ChaincodeStub);
        this.clientIdentity = sinon.createStubInstance(ClientIdentity);
        this.logger = {
            getLogger: sinon.stub().returns(sinon.createStubInstance(winston.createLogger().constructor)),
            setLevel: sinon.stub(),
        };
    }

}

describe('TccContract', () => {

    let contract;
    let ctx;

    beforeEach(() => {
        contract = new TccContract();
        ctx = new TestContext();
        ctx.stub.getState.withArgs('1001').resolves(Buffer.from('{"value":"tcc 1001 value"}'));
        ctx.stub.getState.withArgs('1002').resolves(Buffer.from('{"value":"tcc 1002 value"}'));
    });

    describe('#tccExists', () => {

        it('should return true for a tcc', async () => {
            await contract.tccExists(ctx, '1001').should.eventually.be.true;
        });

        it('should return false for a tcc that does not exist', async () => {
            await contract.tccExists(ctx, '1003').should.eventually.be.false;
        });

    });

    describe('#createTcc', () => {

        it('should create a tcc', async () => {
            await contract.createTcc(ctx, '1003', 'tcc 1003 value');
            ctx.stub.putState.should.have.been.calledOnceWithExactly('1003', Buffer.from('{"value":"tcc 1003 value"}'));
        });

        it('should throw an error for a tcc that already exists', async () => {
            await contract.createTcc(ctx, '1001', 'myvalue').should.be.rejectedWith(/The tcc 1001 already exists/);
        });

    });

    describe('#readTcc', () => {

        it('should return a tcc', async () => {
            await contract.readTcc(ctx, '1001').should.eventually.deep.equal({ value: 'tcc 1001 value' });
        });

        it('should throw an error for a tcc that does not exist', async () => {
            await contract.readTcc(ctx, '1003').should.be.rejectedWith(/The tcc 1003 does not exist/);
        });

    });

    describe('#updateTcc', () => {

        it('should update a tcc', async () => {
            await contract.updateTcc(ctx, '1001', 'tcc 1001 new value');
            ctx.stub.putState.should.have.been.calledOnceWithExactly('1001', Buffer.from('{"value":"tcc 1001 new value"}'));
        });

        it('should throw an error for a tcc that does not exist', async () => {
            await contract.updateTcc(ctx, '1003', 'tcc 1003 new value').should.be.rejectedWith(/The tcc 1003 does not exist/);
        });

    });

    describe('#deleteTcc', () => {

        it('should delete a tcc', async () => {
            await contract.deleteTcc(ctx, '1001');
            ctx.stub.deleteState.should.have.been.calledOnceWithExactly('1001');
        });

        it('should throw an error for a tcc that does not exist', async () => {
            await contract.deleteTcc(ctx, '1003').should.be.rejectedWith(/The tcc 1003 does not exist/);
        });

    });

});

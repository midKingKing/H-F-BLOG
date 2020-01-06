package com.hf.service.impl

import com.github.pagehelper.PageHelper
import com.hf.service.IBaseService
import com.hf.util.MyMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

abstract class BaseServiceImpl<T> : IBaseService<T> {
    @Autowired
    private var baseMapper: MyMapper<T>? = null

    @Transactional(propagation = Propagation.SUPPORTS)
    override fun select(condition: T, pageNum: Int, pageSize: Int): List<T> {
        PageHelper.startPage<Any>(pageNum, pageSize)
        return baseMapper!!.select(condition)
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    override fun selectAll(): List<T> {
        return baseMapper!!.selectAll()
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    override fun selectByPrimaryKey(record: T): T {
        return baseMapper!!.selectByPrimaryKey(record)
    }


    @Transactional(rollbackFor = [Exception::class])
    override fun insert(record: T): T {
        baseMapper!!.insert(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun insertSelective(record: T): T {
        baseMapper!!.insertSelective(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateByPrimaryKey(record: T): T {
        baseMapper!!.updateByPrimaryKey(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun updateByPrimaryKeySelective(record: T): T {
        baseMapper!!.updateByPrimaryKeySelective(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun deleteByPrimaryKey(record: T): Int {
        return baseMapper!!.deleteByPrimaryKey(record)
    }


    @Transactional(rollbackFor = [Exception::class])
    override fun batchDelete(list: List<T>): Int {
        val self = self() as IBaseService<T>
        var c = 0
        for (t in list) {
            c += self.deleteByPrimaryKey(t)
        }
        return c
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun delete(record: T): Int {
        return baseMapper!!.delete(record)
    }
}
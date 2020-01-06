package com.hf.service

import com.github.pagehelper.PageHelper
import com.hf.util.MyMapper
import com.hf.util.ProxySelf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

abstract class BaseService<T> : ProxySelf<BaseService<T>> {
    @Autowired
    private var baseMapper: MyMapper<T>? = null

    @Transactional(propagation = Propagation.SUPPORTS)
    open fun select(condition: T, pageNum: Int, pageSize: Int): List<T> {
        PageHelper.startPage<Any>(pageNum, pageSize)
        return baseMapper!!.select(condition)
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    open fun selectAll(): List<T> {
        return baseMapper!!.selectAll()
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    open fun selectByPrimaryKey(record: T): T {
        return baseMapper!!.selectByPrimaryKey(record)
    }


    @Transactional(rollbackFor = [Exception::class])
    open fun insert(record: T): T {
        baseMapper!!.insert(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun insertSelective(record: T): T {
        baseMapper!!.insertSelective(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun updateByPrimaryKey(record: T): T {
        baseMapper!!.updateByPrimaryKey(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun updateByPrimaryKeySelective(record: T): T {
        baseMapper!!.updateByPrimaryKeySelective(record)
        return record
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun deleteByPrimaryKey(record: T): Int {
        return baseMapper!!.deleteByPrimaryKey(record)
    }


    @Transactional(rollbackFor = [Exception::class])
    open fun batchDelete(list: List<T>): Int {
        val self = self() as BaseService<T>
        var c = 0
        for (t in list) {
            c += self.deleteByPrimaryKey(t)
        }
        return c
    }

    @Transactional(rollbackFor = [Exception::class])
    open fun delete(record: T): Int {
        return baseMapper!!.delete(record)
    }
}
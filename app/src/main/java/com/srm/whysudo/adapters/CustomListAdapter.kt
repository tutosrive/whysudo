/*
 * Copyright (c) 2026 tutosrive. All rights reserved.
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This source code is PROPRIETARY and CONFIDENTIAL.
 * Unauthorized copying, modification, or distribution of this file,
 * via any medium, is strictly prohibited.
 *
 * This software is provided "as is", without warranty of any kind.
 * In no event shall the author be liable for any claim or damages.
 */

package com.srm.whysudo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.srm.whysudo.R
import com.srm.whysudo.utils.DBDataManager
import com.srm.whysudo.utils.ProUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CustomListAdapter(
    val ctx: Context,
    private val commandsList: List<CommandModelView>,
    val dbMan: DBDataManager
) :
    ArrayAdapter<CommandModelView>(ctx, 0, commandsList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var commandView = convertView
        val command: CommandModelView = getItem(position)!!

        if (commandView == null) {
            commandView = LayoutInflater.from(ctx)
                .inflate(
                    R.layout.custom_item_list_commands,
                    parent,
                    false
                )
        }

        val commandText: TextView = commandView.findViewById<TextView>(R.id.commandItem)
        val commandBadge: ImageView = commandView.findViewById<ImageView>(R.id.itemListBadge)
        val favoriteIcon: ImageView = commandView.findViewById<ImageView>(R.id.btnSetFavorite)

        commandText.text = command.filename
        setBadgePro(command, commandBadge)
        setBadgeFavorite(command, favoriteIcon)
        setFavoriteListener(favoriteIcon, command, commandView)

        return commandView
    }

    private fun setFavoriteListener(btn: ImageView, command: CommandModelView, view: View): Unit {
        val commandId: Int = command.id
        btn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val setIsOk: Boolean = dbMan.saveFavorite(commandId)

                if (setIsOk) {
                    command.isFavorite = true
                    setBadgeFavorite(command, btn)
                    Toast.makeText(
                        ctx,
                        "Command Marked As Favorite",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val msg = ctx.getString(R.string.msg_favorite_error)
                    Toast.makeText(
                        ctx,
                        msg,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            this.notifyDataSetChanged()
        }
    }

    private fun isPro(command: CommandModelView): Boolean {
        return command.typeVersion
    }

    private fun isFavorite(command: CommandModelView): Boolean {
        return command.isFavorite
    }

    private fun setBadgePro(cmd: CommandModelView, badge: ImageView): Unit {
        val badgeId = if (isPro(cmd)) R.drawable.prob else R.drawable.freeb
        setDrawable(badgeId, badge)
    }

    private fun setBadgeFavorite(cmd: CommandModelView, badge: ImageView): Unit {
        val drawableId: Int = if (isFavorite(cmd)) {
            R.drawable.ic_star_filled
        } else {
            R.drawable.ic_star
        }
        setDrawable(drawableId, badge)
    }

    private fun setDrawable(id: Int, badge: ImageView): Unit {
        badge.setImageResource(id)
    }
}
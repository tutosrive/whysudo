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
import com.srm.whysudo.R

class CustomListAdapter(val ctx: Context, private val commandsList: List<CommandModelView>) :
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

        commandText.text = command.filename
        setBadge(command, commandBadge)

        return commandView
    }

    private fun isPro(command: CommandModelView): Boolean {
        return command.typeVersion
    }

    private fun setBadge(cmd: CommandModelView, badge: ImageView): Unit {
        val badgeId = if (isPro(cmd)) R.drawable.prob else R.drawable.freeb
        badge.setImageResource(badgeId)
    }
}
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
import com.srm.whysudo.utils.ProUtils

class CustomListAdapter(
    val ctx: Context,
    private val commandsList: List<CommandModelView>,
    val callback: (id: CommandModelView, iconViewer: ImageView) -> Unit
) :
    ArrayAdapter<CommandModelView>(ctx, 0, commandsList) {
    private lateinit var commandText: TextView
    private lateinit var commandBadge: ImageView
    private lateinit var favoriteIcon: ImageView
    private lateinit var command: CommandModelView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initializer(convertView, position, parent)
    }

    private fun initializer(convertView: View?, pos: Int, parent: ViewGroup): View {
        var view = convertView
        command = getItem(pos)!!

        if (view == null) {
            view = LayoutInflater.from(ctx)
                .inflate(
                    R.layout.custom_item_list_commands,
                    parent,
                    false
                )
        }

        getViews(view)
        setDataInViews(view)
        return view
    }

    private fun getViews(view: View): Unit {
        commandText = view.findViewById<TextView>(R.id.commandItem)
        commandBadge = view.findViewById<ImageView>(R.id.itemListBadge)
        favoriteIcon = view.findViewById<ImageView>(R.id.btnSetFavorite)
    }

    private fun setDataInViews(view: View): Unit {
        commandText.text = command.filename
        setBadgePro(command, commandBadge)
        setBadgeFavorite(command, favoriteIcon)
        setFavoriteListener(command, favoriteIcon)
    }

    private fun setFavoriteListener(command: CommandModelView, view: ImageView): Unit {
        view.setOnClickListener {
            callback(command, it as ImageView)
        }
        this.notifyDataSetChanged()
    }

    private fun isPro(command: CommandModelView): Boolean {
        return command.typeVersion
    }

    private fun setBadgePro(cmd: CommandModelView, badge: ImageView): Unit {
        val badgeId = if (isPro(cmd)) R.drawable.prob else R.drawable.freeb
        setDrawable(badgeId, badge)
    }

    private fun setBadgeFavorite(cmd: CommandModelView, badge: ImageView): Unit {
        ProUtils.toggleFavoriteIcon(badge, cmd.isFavorite)
    }

    private fun setDrawable(id: Int, badge: ImageView): Unit {
        badge.setImageResource(id)
    }
}
/*
 * This file is part of the QuickServer library 
 * Copyright (C) 2003-2005 QuickServer.org
 *
 * Use, modification, copying and distribution of this software is subject to
 * the terms and conditions of the GNU Lesser General Public License. 
 * You should have received a copy of the GNU LGP License along with this 
 * library; if not, you can download a copy from <http://www.quickserver.org/>.
 *
 * For questions, suggestions, bug-reports, enhancement-requests etc.
 * visit http://www.quickserver.org
 *
 */

package org.quickserver.util.pool;

import java.util.*;
import org.apache.commons.pool.*;

/**
 * This interface defines a class that creates QSObjectPool from ObjectPool. 
 * Should have a default constructor. 
 * @since 1.4.5
 */
public interface QSObjectPoolMaker {
	public QSObjectPool getQSObjectPool(ObjectPool objectPool);
}

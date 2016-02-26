/*
 * Copyright 2015 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.util.cache;

import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;

/**
 * JCache expiry policy to expire entries 1 day after creation or update, regardless on how often they are retrieved.
 * <p>
 * This is normally used for caching Darwin queries as it's a license requirement to not show anything older than 1 minute.
 * <p>
 * @author peter
 */
public class DailyExpiryPolicy
        implements ExpiryPolicy
{

    @Override
    public Duration getExpiryForCreation()
    {
        return Duration.ONE_DAY;
    }

    @Override
    public Duration getExpiryForAccess()
    {
        return null;
    }

    @Override
    public Duration getExpiryForUpdate()
    {
        return Duration.ONE_DAY;
    }

}

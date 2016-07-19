package com.ybg.rp.ua.partner

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class PartnerUserAuthority implements Serializable {

    static belongsTo = [user: PartnerUserInfo, authority: PartnerAuthorityInfo]

    PartnerUserInfo user
    PartnerAuthorityInfo authority

    @Override
    boolean equals(other) {
        if (other instanceof PartnerUserAuthority) {
            other.userId == user?.id && other.authorityId == authority?.id
        }
    }

    @Override
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (authority) builder.append(authority.id)
        builder.toHashCode()
    }

    static PartnerUserAuthority get(long userId, long authorityId) {
        criteriaFor(userId, authorityId).get()
    }

    static boolean exists(long userId, long authorityId) {
        criteriaFor(userId, authorityId).count()
    }

    private static DetachedCriteria criteriaFor(long userId, long authorityId) {
        PartnerUserAuthority.where {
            user == PartnerUserInfo.load(userId) &&
                    authority == PartnerAuthorityInfo.load(authorityId)
        }
    }

    static PartnerUserAuthority create(PartnerUserInfo user, PartnerAuthorityInfo authority) {
        def instance = new PartnerUserAuthority(user: user, authority: authority)
        instance.save()
        instance
    }

    static PartnerUserAuthority create(Long userId, Long authorityId) {
        def user = PartnerUserInfo.get(userId)
        def authority = PartnerAuthorityInfo.get(authorityId)
        if (user && authority) {
            def instance = new PartnerUserAuthority(user: user, authority: authority)
            instance.save(flush: true)
            instance
        }
    }

    static boolean remove(PartnerUserInfo u, PartnerAuthorityInfo r) {
        if (u != null && r != null) {
            PartnerUserAuthority.where { user == u && authority == r }.deleteAll()
        }
    }

    static int removeAll(PartnerUserInfo u) {
        u == null ? 0 : PartnerUserAuthority.where { user == u }.deleteAll()
    }

    static int removeAll(PartnerAuthorityInfo r) {
        r == null ? 0 : PartnerUserAuthority.where { authority == r }.deleteAll()
    }

    static constraints = {
        authority validator: { PartnerAuthorityInfo r, PartnerUserAuthority ur ->
            if (ur.user?.id) {
                PartnerUserAuthority.withNewSession {
                    if (PartnerUserAuthority.exists(ur.user.id, r.id)) {
                        return ['userRole.exists']
                    }
                }
            }
        }
    }

    static mapping = {
        id composite: ['user', 'authority']
        version false
    }
}

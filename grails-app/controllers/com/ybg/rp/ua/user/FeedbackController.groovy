package com.ybg.rp.ua.user

class FeedbackController {

    def create() {

    }

    def save(Feedback feedback) {
        if (feedback) {
            feedback.createTime = new Date()
            feedback.save flush: true
        }
        render(view: "save")
    }

    def show(Feedback feedback) {
        respond feedback
    }

    def list(String openid) {

    }

    def listAll() {

    }

    def view(String viewName) {
        render(view: viewName)
    }

}

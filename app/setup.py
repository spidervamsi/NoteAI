from flask import Flask
# from flask_swagger_ui import get_swaggerui_blueprint
from engine_api import engine_api_blueprint



def create_app():
    app = Flask(__name__)

    print("Create app")

    # app.config.update(dict(
    #     SECRET_KEY="powerful secretkey",
    #     WTF_CSRF_SECRET_KEY="a csrf secret key",
    #     SQLALCHEMY_DATABASE_URI='mysql+mysqlconnector://root:test@product_db/product',
    #     SQLALCHEMY_TRACK_MODIFICATIONS=False
    # ))


    app.register_blueprint(engine_api_blueprint)

    return app
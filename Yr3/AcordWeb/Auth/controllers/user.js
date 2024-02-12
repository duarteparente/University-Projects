// Controlador para o modelo User

var User = require('../models/user')

// Lista de Users
module.exports.list = () => {
    return User
            .find()
            .sort('name')
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


// User Logout
module.exports.userLogout = usr => {
    return User.updateOne({username: usr}, {active: false})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

// User Login
module.exports.userLogin = (usr, date) => {
    return User.updateOne({username: usr}, {active: true, lastAccess: date})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


module.exports.getUser = username => {
    return User.findOne({username: username})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


module.exports.addUser = u => {
    return User.create(u)
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}


module.exports.updateUser = (usr, info) => {
    return User.updateOne({username:usr}, info)
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

module.exports.updateUserStatus = (id, status) => {
    return User.updateOne({username: id}, {active: status})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

module.exports.deleteUser = usr => {
    return User.deleteOne({username:usr})
            .then(resposta => {
                return resposta
            })
            .catch(erro => {
                return erro
            })
}

module.exports.addFav = (username, fav) => {
    return User.updateOne({ username: username }, { $push: { favs: fav } })
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}


module.exports.elimina = (username, fav) => {
    return User.updateMany(
      {
        $or: [
          { favs: { $elemMatch: { Processo: fav.Processo } } },
          { added: fav.Processo },
          { edited: fav.Processo }
        ]
      },
      {
        $pull: {
          favs: { Processo: fav.Processo },
          added: fav.Processo,
          edited: fav.Processo
        }
      }
    )
      .then(resposta => {
        return resposta;
      })
      .catch(erro => {
        return erro;
      });
};
  
  
module.exports.updateFoto = (username, photo) => {
    return User.updateOne({ username: username }, {image: photo})
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}

module.exports.addRegisto = (username, ID) => {
    return User.updateOne({ username: username }, { $push: { added: ID } })
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}

module.exports.addEdit = (username, ID) => {
    return User.updateOne({ username: username }, { $push: { edited: ID } })
        .then(resposta => {
            return resposta
        })
        .catch(erro => {
            return erro
        })
}
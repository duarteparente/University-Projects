var axios = require('axios')

// Tasks list
module.exports.list = () => {
    return axios.get("http://localhost:3000/tasks?_sort=due_date")
            .then(resposta => {
                return resposta.data
            })
            .catch(erro => {
                return erro
            })
}

module.exports.getTask = id => {
    return axios.get('http://localhost:3000/tasks/' + id)
            .then(resposta => {
                return resposta.data
            })
            .catch(erro => {
                return erro
            })
}

module.exports.addTask = task => {
    return axios.post('http://localhost:3000/tasks', task)
            .then(resposta => {
                return resposta.data
            })
            .catch(erro => {
                return erro
            })
}

module.exports.updateTask = task => {
    return axios.put('http://localhost:3000/tasks/' + task.id, task)
            .then(resposta => {
                console.log(task)
                return resposta.data
            })
            .catch(erro => {
                return erro
            })
}

module.exports.deleteTask = id => {
    return axios.delete('http://localhost:3000/tasks/' + id)
            .then(resposta => {
                return resposta.data
            })
            .catch(erro => {
                return erro
            })
}
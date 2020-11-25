package client.petmooby.com.br.petmooby.application

import client.petmooby.com.br.petmooby.service.AnimalService
import client.petmooby.com.br.petmooby.ui.repository.AnimalRepository
import client.petmooby.com.br.petmooby.ui.viewmodel.AnimalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



    val repositories = module {
        factory { AnimalRepository() }
    }

    val services = module {
        factory { AnimalService(get()) }
    }

    val viewModels = module {
        viewModel { AnimalViewModel(get())}
    }

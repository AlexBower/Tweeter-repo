package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenter {

    private final View view;

    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }
    public RegisterPresenter(View view) {
        this.view = view;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws IOException, TweeterRemoteException {
        RegisterServiceProxy registerService = getRegisterService();
        return registerService.register(registerRequest);
    }

    RegisterServiceProxy getRegisterService() { return new RegisterServiceProxy(); }
}
// Generated C++ file by Il2CppInspector - http://www.djkaty.com - https://github.com/djkaty
// IL2CPP application initializer

#include "il2cpp-appdata.h"
#include "il2cpp-init.h"
// IL2CPP APIs
#define DO_API(r, n, p) r (*n) p
#include "il2cpp-api-functions.h"
#undef DO_API

// Application-specific functions
#define DO_APP_FUNC(a, r, n, p) r (*n) p
namespace app {
#include "il2cpp-functions.h"
}
#undef DO_APP_FUNC

// TypeInfo pointers
#define DO_TYPEDEF(a, n) n ## __Class* n ## __TypeInfo
namespace app {
#include "il2cpp-types-ptr.h"
}
#undef DO_TYPEDEF

// IL2CPP application initializer
void init_il2cpp(long baseAddress)
{
	// Get base address of IL2CPP module
	using namespace app;

	// Define IL2CPP API function addresses
	#define DO_API(r, n, p) n = (r (*) p)(baseAddress + n ## _ptr)
	#include "il2cpp-api-functions.h"
	#undef DO_API

	// Define function addresses
	#define DO_APP_FUNC(a, r, n, p) n = (r (*) p)(baseAddress + a)
	#include "il2cpp-functions.h"
	#undef DO_APP_FUNC

	// Define TypeInfo variables
	#define DO_TYPEDEF(a, n) n ## __TypeInfo = *(n ## __Class**) (baseAddress + a);
	#include "il2cpp-types-ptr.h"
	#undef DO_TYPEDEF
}